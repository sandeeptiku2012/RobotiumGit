package no.sumo.api.vo.product;

import no.sumo.api.contracts.IPaymentPlan;
import no.sumo.api.entity.sumo.enums.PaymentType;
import no.sumo.api.vo.RestObject;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root( name = "period" )
@Default( DefaultType.FIELD )
public class RestPaymentPlan extends RestObject implements IPaymentPlan {
	@Attribute( name = "id" )
	private Long id;

	@Element( name = "name" )
	private String name;

	@Element( name = "period" )
	private long period;

	@Element( name = "paymentType" )
	private PaymentType paymentType;

	public RestPaymentPlan() {
	}

	@Override
	public long getId() {
		return id;
	}

	public void setId( Long id ) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}

	@Override
	public long getPeriod() {
		return period;
	}

	public void setPeriod( Long period ) {
		this.period = period;
	}

	@Override
	public PaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType( PaymentType paymentType ) {
		this.paymentType = paymentType;
	}

}
