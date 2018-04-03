package com.d1m.wechat.dto;

import java.util.List;

public class ConversationDto {

	/** conversation id */
	private Integer cid;

	/** mass conversation result id */
	private Integer id;

	private Byte msgType;

	private String memberPhoto;

	private String memberId;

	private String memberNickname;

	private String memberOpenId;

	private String kfPhoto;

	private String content;

	private String createdAt;

	private String current;

	private List<ImageTextDto> items;

	private Integer dir;

	private Integer isMass;

	private MassConversationResultDto result;

	private Byte status;

	private Integer materialId;

	private Byte event;

	private String sendAt;

	private String wxSendAt;

	private String auditAt;

	private String auditBy;

	private String reason;

	private String runAt;

	private String voiceUrl;

	private String videoUrl;

	public String getVoiceUrl() {
		return voiceUrl;
	}

	public void setVoiceUrl(String voiceUrl) {
		this.voiceUrl = voiceUrl;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public String getAuditAt() {
		return auditAt;
	}

	public String getAuditBy() {
		return auditBy;
	}

	public Integer getCid() {
		return cid;
	}

	public String getContent() {
		return content;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public String getCurrent() {
		return current;
	}

	public Integer getDir() {
		return dir;
	}

	public Byte getEvent() {
		return event;
	}

	public Integer getId() {
		return id;
	}

	public Integer getIsMass() {
		return isMass;
	}

	public List<ImageTextDto> getItems() {
		return items;
	}

	public String getKfPhoto() {
		return kfPhoto;
	}

	public Integer getMaterialId() {
		return materialId;
	}

	public String getMemberId() {
		return memberId;
	}

	public String getMemberNickname() {
		return memberNickname;
	}

	public String getMemberOpenId() {
		return memberOpenId;
	}

	public String getMemberPhoto() {
		return memberPhoto;
	}

	public Byte getMsgType() {
		return msgType;
	}

	public String getReason() {
		return reason;
	}

	public MassConversationResultDto getResult() {
		return result;
	}

	public String getRunAt() {
		return runAt;
	}

	public String getSendAt() {
		return sendAt;
	}

	public Byte getStatus() {
		return status;
	}

	public String getWxSendAt() {
		return wxSendAt;
	}

	public void setAuditAt(String auditAt) {
		this.auditAt = auditAt;
	}

	public void setAuditBy(String auditBy) {
		this.auditBy = auditBy;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public void setCurrent(String current) {
		this.current = current;
	}

	public void setDir(Integer dir) {
		this.dir = dir;
	}

	public void setEvent(Byte event) {
		this.event = event;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setIsMass(Integer isMass) {
		this.isMass = isMass;
	}

	public void setItems(List<ImageTextDto> items) {
		this.items = items;
	}

	public void setKfPhoto(String kfPhoto) {
		this.kfPhoto = kfPhoto;
	}

	public void setMaterialId(Integer materialId) {
		this.materialId = materialId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public void setMemberNickname(String memberNickname) {
		this.memberNickname = memberNickname;
	}

	public void setMemberOpenId(String memberOpenId) {
		this.memberOpenId = memberOpenId;
	}

	public void setMemberPhoto(String memberPhoto) {
		this.memberPhoto = memberPhoto;
	}

	public void setMsgType(Byte msgType) {
		this.msgType = msgType;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public void setResult(MassConversationResultDto result) {
		this.result = result;
	}

	public void setRunAt(String runAt) {
		this.runAt = runAt;
	}

	public void setSendAt(String sendAt) {
		this.sendAt = sendAt;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public void setWxSendAt(String wxSendAt) {
		this.wxSendAt = wxSendAt;
	}

}
