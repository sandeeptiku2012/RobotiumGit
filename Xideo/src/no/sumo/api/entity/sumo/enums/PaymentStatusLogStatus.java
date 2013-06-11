package no.sumo.api.entity.sumo.enums;

public class PaymentStatusLogStatus {

	public static final int REQUEST = 1;
	public static final int AUTHORIZE = 2;
	public static final int RESULT = 3;
	public static final int COMPLETE = 4;
	public static final int CHECK = 5;
	public static final int SALECC = 6;
	public static final int STOP = 7;
	public static final int SALEEXTERNALLOGIN = 9;
	public static final int ADD_ORDER = 10;
	public static final int CREDIT = 50;

	public static final int REQUEST_FAILED = 101;
	public static final int AUTHORIZE_FAILED = 102;
	public static final int RESULT_FAILED = 103;
	public static final int COMPLETE_FAILED = 104;
	public static final int CHECK_FAILED = 105;
	public static final int SALECC_FAILED = 106;
	public static final int STOP_FAILED = 107;
	public static final int SALEEXTERNALLOGIN_FAILED = 109;
	public static final int ADD_ORDER_FAILED = 110;
	public static final int CREDIT_FAILED = 150;

	public static final int ALREADY_CAPTURED = 201;
	public static final int CAPTURE_FAILED = 202;
	public static final int CAPTURE_FAILED_AUTORENEW = 203;

	public static final int UPDATE_PROFILE_FAILED = 301;
	public static final int RULES_INCONCISTENCY = 302;

	public static final int AUTORENEW_EXTENSION = 303;

}
