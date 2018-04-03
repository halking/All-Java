package com.d1m.wechat.mapper;

import com.d1m.wechat.model.MemberScanQrcode;
import com.d1m.wechat.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MemberScanQrcodeMapper extends MyMapper<MemberScanQrcode> {

    List<MemberScanQrcode> getMemberScanQrcodeList(@Param("memberId") Integer memberId, @Param("wechatId") Integer wechatId);
}