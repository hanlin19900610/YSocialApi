package com.yin.android.sociallibrary;

/**
 * description :
 *
 * @author : yinzeyu
 * @date : 2018/8/25 15:00
 */
public class PayBean {
  private String params;

  private String out_trade_no;

  private String appid;
  private String noncestr;
  private String packageX;
  private String partnerid;
  private String prepayid;
  private String timestamp;
  private String orderNumber;
  private String sign;
  /**
   * 提交订单type
   */
  private int submitPayType;

  public String getOrderNumber() {
    return orderNumber;
  }

  public void setOrderNumber(String orderNumber) {
    this.orderNumber = orderNumber;
  }

  public String getOut_trade_no() {
    return out_trade_no;
  }

  public void setOut_trade_no(String out_trade_no) {
    this.out_trade_no = out_trade_no;
  }

  public String getSign() {
    return sign;
  }

  public void setSign(String sign) {
    this.sign = sign;
  }

  public String getParams() {
    return params;
  }

  public void setParams(String params) {
    this.params = params;
  }

  public String getAppid() {
    return appid;
  }

  public void setAppid(String appid) {
    this.appid = appid;
  }

  public String getNoncestr() {
    return noncestr;
  }

  public void setNoncestr(String noncestr) {
    this.noncestr = noncestr;
  }

  public String getPackageX() {
    return packageX;
  }

  public void setPackageX(String packageX) {
    this.packageX = packageX;
  }

  public String getPartnerid() {
    return partnerid;
  }

  public void setPartnerid(String partnerid) {
    this.partnerid = partnerid;
  }

  public String getPrepayid() {
    return prepayid;
  }

  public void setPrepayid(String prepayid) {
    this.prepayid = prepayid;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public int getSubmitPayType() {
    return submitPayType;
  }

  public void setSubmitPayType(int submitPayType) {
    this.submitPayType = submitPayType;
  }
}
