package com.sse.sseapp.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.exception.AppException;
import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.office.OpenOffice;
import com.sse.sseapp.domain.system.SysDictData;
import com.sse.sseapp.feign.office.IOpenOfficeFeign;
import com.sse.sseapp.feign.system.ISysConfigFeign;
import com.sse.sseapp.feign.system.ISysDictDataFeign;
import com.sse.sseapp.form.request.OfficeReqBody;
import com.sse.sseapp.form.response.OfficeResBody;
import com.sse.sseapp.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


/**
 * 文件转换
 *
 * @author wy
 * @date 2023-06-02
 */
@Service
@Slf4j
public class OfficeService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private IOpenOfficeFeign openOfficeFeign;

    @Autowired
    private ISysConfigFeign sysConfigFeign;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    ISysDictDataFeign sysDictDataFeign;

    /**
     * 文件转换pdf
     *
     * @param officeReqBodyBaseRequest 入参
     */
    @Transactional(rollbackFor = Exception.class)
    public OfficeResBody convertPdf(BaseRequest<OfficeReqBody> officeReqBodyBaseRequest) {
        String sourceFileUrl = officeReqBodyBaseRequest.getReqContent().getSourceFileUrl();
        log.info("源文件地址：" + sourceFileUrl);
        //判断开头是否有http
        if (!sourceFileUrl.startsWith("http")) {
            sourceFileUrl = "http://" + sourceFileUrl;
        }
        if (sourceFileUrl.startsWith("https")) {
            sourceFileUrl = sourceFileUrl.replace("https", "http");
        }
        if (sourceFileUrl.contains("?")) {
            sourceFileUrl = sourceFileUrl.substring(0, sourceFileUrl.indexOf("?"));
        }
        //验证url是否有效
        if (!isUrl(sourceFileUrl)) {
            throw new AppException("文件地址格式不正确");
        }
        OfficeResBody officeResBody = new OfficeResBody();
        //上锁
        if (lock(sourceFileUrl)) {
            try {
                //允许下载文件域名白名单
                AjaxResult ajaxResult = sysDictDataFeign.dictType("open_office_url");
                List<SysDictData> whiteList = JSONUtil.parseArray(ajaxResult.get("data")).toList(SysDictData.class);
                //查询域名是否在白名单里
                URL url = new URL(sourceFileUrl);
                // 获取主机名
                String hostName = url.getHost();
                Optional<String> optional = whiteList.stream().filter(a -> ObjectUtil.equal(hostName, a.getDictLabel())).map(SysDictData::getDictValue).findAny();
                if (!optional.isPresent()) {
                    throw new AppException("文件地址不在允许范围内");
                }
                //文件格式
                String fileType = sourceFileUrl.substring(sourceFileUrl.lastIndexOf(".")).toLowerCase();
                //文件名称
                String fileName = sourceFileUrl.substring(sourceFileUrl.lastIndexOf("/") + 1, sourceFileUrl.lastIndexOf("."));
                // pdf后缀
                String pdfSuffix = ".pdf";
                //如果是pdf，直接返回
                if (ObjectUtil.equal(fileType, pdfSuffix)) {
                    officeResBody.setFileUrl(sourceFileUrl);
                    return officeResBody;
                }
                //从数据库查询配置
                List<String> configList = Arrays.asList(sysConfigFeign.getConfigKey("libreOffice").split(";"));
                //libreOffice版本
                String libreOfficeVersion = Arrays.asList(configList.get(0).split("\\|")).get(1);
                // 源文件存储目的地目录
                String destinationDir = Arrays.asList(configList.get(1).split("\\|")).get(1);
                //存储转换pdf之后的目录
                String outputPdfDir = Arrays.asList(configList.get(2).split("\\|")).get(1);
                //外网url
                String prefixUrl = Arrays.asList(configList.get(3).split("\\|")).get(1);
                //模拟游览器请求，防止屏蔽程序抓取
                HttpResponse response = HttpUtil
                        .createGet(sourceFileUrl, true)
                        .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.5005.63 Safari/537.36 Edg/102.0.1245.39")
                        .timeout(-1).executeAsync();
                if (ObjectUtil.notEqual(response.getStatus(), 200)) {
                    log.error("文件获取失败：" + response.getStatus());
                    throw new AppException("文件获取失败");
                }
                //新建文件
                File sourceFile = new File(destinationDir + fileName + fileType);
                response.bodyBytes();
                response.writeBody(sourceFile);
                //获取文件md5
                String fileMd5 = DigestUtils.md5Hex(new FileInputStream(sourceFile.getPath()));
                log.info("文件MD5" + fileMd5);
                boolean existsFlag = true;
                String updatePath = null;
                //从redis查找
                String cacheFile = redisService.getCacheObject("openoffice:" + fileMd5);
                log.info("cacheFile:"+cacheFile);
                if (ObjectUtil.isNotEmpty(cacheFile)) {
                    officeResBody.setFileUrl(cacheFile);
                    cacheFile = cacheFile.replace(prefixUrl,outputPdfDir);
                    log.info("转换文件地址："+  cacheFile);
                    File file = new File(cacheFile);
                    existsFlag = file.exists();
                    if (existsFlag) {
                        return officeResBody;
                    }
                }
                //获取redis存储时间
                String redisCacheTime = sysConfigFeign.getConfigKey("libreOffice_cache_time");
                //从数据库查找
                OpenOffice outFileUrl = openOfficeFeign.selectOutputBySource(sourceFileUrl);
                if (ObjectUtil.isNotEmpty(outFileUrl)) {
                    //比对md5是否一致
                    if (ObjectUtil.equal(outFileUrl.getMd5(), fileMd5)) {
                        officeResBody.setFileUrl(outFileUrl.getOutput());
                        log.info("转换文件地址："+  outFileUrl.getOutput().replace(prefixUrl,outputPdfDir));
                        updatePath = outFileUrl.getOutput().replace(prefixUrl,outputPdfDir);
                        File file = new File(updatePath);
                        existsFlag = file.exists();
                        log.info("判断是否存在该文件："+existsFlag);
                        if (existsFlag){
                            //缓存到redis
                            redisService.setCacheObject("openoffice:" + outFileUrl.getMd5(), outFileUrl.getOutput(), Long.parseLong(redisCacheTime), TimeUnit.HOURS);
                            return officeResBody;
                        }
                    } else {
                        //md5不一致删除旧的记录
                        openOfficeFeign.deleteById(outFileUrl.getId());
                    }
                }
                log.info("开始转为PDF===========");
                // 开始时间
                long startTime = System.currentTimeMillis();
                //文件转化
                if (!existsFlag) {
                    updatePath = updatePath.substring(0,updatePath.lastIndexOf("/"));
                    convertPDF(libreOfficeVersion, sourceFile.getPath(), updatePath);
                    updatePath = cacheFile.replace(outputPdfDir,prefixUrl);
                    redisService.setCacheObject("openoffice:" + fileMd5, updatePath, Long.parseLong(redisCacheTime), TimeUnit.HOURS);
                    return officeResBody;
                }
                String outputPath = outputPdfDir + LocalDate.now().toString().replace("-", "/") + "/" + fileType.replace(".", "");
                convertPDF(libreOfficeVersion, sourceFile.getPath(), outputPath);
                //对外文件url
                prefixUrl = prefixUrl + LocalDate.now().toString().replace("-", "/") + "/" + fileType.replace(".", "") + "/" + fileName + pdfSuffix;
                // 结束时间
                long endTime = System.currentTimeMillis();
                log.info("转换时间：" + (endTime - startTime) + "ms");
                //保存到数据库
                log.info("sourceFileUrl：" + sourceFileUrl);
                log.info("prefixUrl：" + prefixUrl);
                log.info("fileMd5：" + fileMd5);
                OpenOffice openOffice = new OpenOffice();
                openOffice.setSource(sourceFileUrl);
                openOffice.setOutput(prefixUrl);
                openOffice.setMd5(fileMd5);
                int row = openOfficeFeign.insert(openOffice);
                if (row > 0) {
                    //缓存到redis
                    redisService.setCacheObject("openoffice:" + fileMd5, prefixUrl, Long.parseLong(redisCacheTime), TimeUnit.HOURS);
                }
                log.info("插入数据库结果：" + row);
                //删除源文件
                sourceFile.delete();
                officeResBody.setFileUrl(prefixUrl);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("文件转换失败：" + e.getMessage());
                throw new AppException("文件转换失败，请稍后再试");
            } finally {
                unlock(sourceFileUrl);
            }
        }
        return officeResBody;
    }


    /**
     * 验证url格式是否正确
     *
     * @param str 源文件地址
     */
    public static boolean isUrl(String str) {
        str = str.toLowerCase();
        String regex = "^((https|http)?://)"
                + "(([0-9]{1,3}\\.){3}[0-9]{1,3}" // IP形式的URL- 例如：199.194.52.184
                + "|" // 允许IP和DOMAIN(域名)
                + "([0-9a-z_!~*'()-]+\\.)*" // 域名- www.
                + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\." // 二级域名
                + "[a-z]{2,6})" // first level domain- .com or .museum
                + "(:[0-9]{1,5})?" // 端口号最大为65535,5位数
                + "((/?)|" // a slash isn't required if there is no file name
                + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)"
                + "(.doc|.docx|.xls|.xlsx|.pdf)$"; //文件格式
        return str.matches(regex);
    }

    /**
     * 加锁
     *
     * @param lockId 锁id
     */
    public boolean lock(String lockId) {
        Boolean success = redisTemplate.opsForValue().setIfAbsent(lockId, "转换中", 60, TimeUnit.SECONDS);
        return success != null && success;
    }

    /**
     * 解锁
     *
     * @param lockId 锁id
     */
    public void unlock(String lockId) {
        redisTemplate.delete(lockId);
    }

    /**
     * 利用 LibreOffice 将 Office 文档转换成 PDF
     *
     * @param libreOfficeVersion 版本
     * @param filePath           目标文件地址
     * @param targetFilePath     输出文件夹
     * @return 子线程执行完毕的返回值
     */
    public static int convertPDF(String libreOfficeVersion, String filePath, String targetFilePath) throws Exception {
        String command = libreOfficeVersion + " --headless --invisible --convert-to pdf:writer_pdf_Export " + filePath + " --outdir " + targetFilePath;
        log.info("转换命令：" + command);
        Process process = Runtime.getRuntime().exec(command);
        int exitStatus = process.waitFor();
        if (exitStatus == 0) {
            exitStatus = process.exitValue();
        }
        // 销毁子进程
        process.destroy();
        return exitStatus;
    }
}
