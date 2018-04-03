package com.d1m.wechat.service;

import com.d1m.wechat.model.BookOnAppointment;
import com.d1m.wechat.model.Member;
import com.d1m.wechat.model.MemberProfile;
import com.d1m.wechat.pamametermodel.ZegnaModel;

/**
 * Created by D1M on 2017/5/3.
 */
public interface BookOnAppointmentService extends IService<BookOnAppointment> {
    BookOnAppointment book(ZegnaModel model);

    boolean isBooked(Integer memberId);

    void logMember(ZegnaModel model);
}
