package com.d1m.wechat.service.impl;

import com.d1m.wechat.mapper.BookOnAppointmentMapper;
import com.d1m.wechat.mapper.ConfigMapper;
import com.d1m.wechat.mapper.ZegnaMemberBaaLogMapper;
import com.d1m.wechat.model.BookOnAppointment;
import com.d1m.wechat.model.Member;
import com.d1m.wechat.model.ZegnaMemberBaaLog;
import com.d1m.wechat.pamametermodel.ZegnaModel;
import com.d1m.wechat.service.BookOnAppointmentService;
import com.d1m.wechat.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;

/**
 * Created by D1M on 2017/5/3.
 */
@Service
public class BookOnAppointmentServiceImpl extends BaseService<BookOnAppointment> implements BookOnAppointmentService {
    @Autowired
    private BookOnAppointmentMapper bookOnAppointmentMapper;

    @Autowired
    private ZegnaMemberBaaLogMapper zegnaMemberBaaLogMapper;


    @Override
    public Mapper<BookOnAppointment> getGenericMapper() {
        return bookOnAppointmentMapper;
    }

    @Override
    public BookOnAppointment book(ZegnaModel model) {
        BookOnAppointment boa = new BookOnAppointment();
        boa.setMemberId(model.getMemberId());
        List<BookOnAppointment> boaList = bookOnAppointmentMapper.select(boa);
        if(boaList != null && boaList.size() > 0) {
            return null;
        }
        boa.setBookDate(DateUtil.parse(model.getDate()));
        boa.setStoreId(model.getStoreId());
        boa.setBookTime(model.getTime());
        bookOnAppointmentMapper.insert(boa);
        return boa;
    }

    @Override
    public boolean isBooked(Integer memberId) {
        BookOnAppointment boa = new BookOnAppointment();
        boa.setMemberId(memberId);
        BookOnAppointment bookOnAppointment = bookOnAppointmentMapper.selectOne(boa);
        return bookOnAppointment != null;
    }

    @Override
    public void logMember(ZegnaModel model) {
        ZegnaMemberBaaLog zegnaMemberLog = new ZegnaMemberBaaLog();
        zegnaMemberLog.setMemberId(model.getMemberId());
        zegnaMemberLog.setGoodsId(model.getGoodsId());
        ZegnaMemberBaaLog existedLog = zegnaMemberBaaLogMapper.selectOne(zegnaMemberLog);
        if (existedLog != null) {
            return;
        }
        zegnaMemberLog.setDate(new Date());
        zegnaMemberBaaLogMapper.insert(zegnaMemberLog);
    }
}
