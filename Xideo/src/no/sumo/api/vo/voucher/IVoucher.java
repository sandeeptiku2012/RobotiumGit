package no.sumo.api.vo.voucher;

import java.util.Date;

public interface IVoucher {

	public VoucherType getVoucherType();

	public void setVoucherType( VoucherType voucherType );

	public DiscountType getDiscountType();

	public void setDiscountType( DiscountType discountType );

	public String getCode();

	public void setCode( String code );

	public String getPool();

	public void setPool( String pool );

	public Date getStartDate();

	public void setStartDate( Date startDate );

	public Date getExpiry();

	public void setExpiry( Date expiry );

	public Date getRegistered();

	public void setRegistered( Date registered );

	public Integer getUsages();

	public void setUsages( Integer usages );

	public Boolean getAvailable();

	public void setAvailable( Boolean available );

	public Double getDiscount();

	public void setDiscount( Double discount );

	public Double getDiscountFraction();

	public void setDiscountFraction( Double discountFraction );

	public Long getProductPaymentId();

	public void setProductPaymentId( Long productPaymentId );

}