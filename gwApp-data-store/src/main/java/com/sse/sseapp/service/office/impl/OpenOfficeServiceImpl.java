package com.sse.sseapp.service.office.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sse.sseapp.core.utils.uuid.IdUtils;
import com.sse.sseapp.core.utils.uuid.SnowUtils;
import com.sse.sseapp.domain.office.OpenOffice;
import com.sse.sseapp.mapper.office.OpenOfficeMapper;
import com.sse.sseapp.service.office.OpenOfficeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author wy
 * @date 2023-06-03
 */
@Service
@Slf4j
public class OpenOfficeServiceImpl extends ServiceImpl<OpenOfficeMapper, OpenOffice> implements OpenOfficeService {

    @Autowired
    private OpenOfficeMapper officeMapper;

    /**
     * 查询全部
     *
     * @return
     */
    @Override
    public List<OpenOffice> selectOpenOfficeList() {
        QueryWrapper<OpenOffice> queryWrapper = new QueryWrapper<>();
        return this.officeMapper.selectList(queryWrapper);
    }

    /**
     * 新增
     *
     * @param openOffice
     * @return
     */
    @Override
    public int insert(OpenOffice openOffice) {
        openOffice.setId(IdUtils.randomUUID());
        openOffice.setCreateTime(new Date());
        return this.officeMapper.insertOpenOffice(openOffice);
    }

    /**
     * 根据来源文件查询
     *
     * @param source
     * @return
     */
    @Override
    public OpenOffice selectOutputBySource(String source) {
        return this.officeMapper.selectOutputBySource(source);
    }

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    @Override
    public int deleteById(String id) {
        return this.officeMapper.deleteById(id);
    }
}
