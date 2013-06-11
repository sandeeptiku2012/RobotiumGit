package no.sumo.api.vo.profile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import no.sumo.api.contracts.ConversionFactory;
import no.sumo.api.contracts.IUser;
import no.sumo.api.contracts.IUserProperty;
import no.sumo.api.vo.RestObject;
import no.sumo.api.vo.notification.NotificationType;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root( name = "user" )
@Default( DefaultType.FIELD )
public class RestUser extends RestObject implements IUser {

	private String address;
	private String city;
	private String country;
	private Date dateOfBirth;
	private String email;
	private Integer emailStatus;
	private String firstName;
	private Integer gender;
	private Long id;
	private Boolean infoRestricted;
	private String lastName;
	private String lowSecurityToken;
	private String mobileNumber;
	private Integer mobileStatus;
	private String nick;
	private String password;
	private String zip;
	private Boolean receiveEmail;
	private Boolean receiveSms;
	private Date registrationDate;
	private String userName;

	//not part of the user domain object
	private NotificationType notifyUserOnCreation;

	@ElementList( name = "properties", entry = "property" )
	private List<RestUserProperty> properties;

	public RestUser() {
	}

	public static RestUser getMock() {

		RestUser user = new RestUser();
		user.setId( 123456789L );

		user.setFirstName( "Tor Erik" );
		user.setLastName( "Alr_k" );
		user.setMobileNumber( "+47 98821212" );
		user.setGender( 0 );

		user.setEmail( "dummy" + Math.abs( new Random().nextInt() ) + ".email@foo.bar.com" );
		user.setEmailStatus( 1 );

		user.setAddress( "N_stegaten 72" );
		user.setZip( "5020" );
		user.setCity( "Bergen" );

		user.setInfoRestricted( false );
		user.setNick( "testapi" + Math.abs( new Random().nextInt() ) );
		user.setUserName( "testapi" + Math.abs( new Random().nextInt() ) );
		user.setPassword( "qqqq" );

		return user;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress( String address ) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity( String city ) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry( String country ) {
		this.country = country;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth( Date dateOfBirth ) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail( String email ) {
		this.email = email;
	}

	public Integer getEmailStatus() {
		return emailStatus;
	}

	public void setEmailStatus( Integer emailStatus ) {
		this.emailStatus = emailStatus;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName( String firstName ) {
		this.firstName = firstName;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender( Integer gender ) {
		this.gender = gender;
	}

	public Long getId() {
		return id;
	}

	public void setId( Long id ) {
		this.id = id;
	}

	public boolean isInfoRestricted() {
		return infoRestricted;
	}

	public void setInfoRestricted( Boolean infoRestricted ) {
		this.infoRestricted = infoRestricted;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName( String lastName ) {
		this.lastName = lastName;
	}

	public String getLowSecurityToken() {
		return lowSecurityToken;
	}

	public void setLowSecurityToken( String lowSecurityToken ) {
		this.lowSecurityToken = lowSecurityToken;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber( String mobileNumber ) {
		this.mobileNumber = mobileNumber;
	}

	public Integer getMobileStatus() {
		return mobileStatus;
	}

	public void setMobileStatus( Integer mobileStatus ) {
		this.mobileStatus = mobileStatus;
	}

	public String getNick() {
		return nick;
	}

	public void setNick( String nick ) {
		this.nick = nick;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword( String password ) {
		this.password = password;
	}

	public String getZip() {
		return zip;
	}

	public void setZip( String zip ) {
		this.zip = zip;
	}

	public boolean isReceiveEmail() {
		return receiveEmail;
	}

	public void setReceiveEmail( Boolean receiveEmail ) {
		this.receiveEmail = receiveEmail;
	}

	public boolean isReceiveSms() {
		return receiveSms;
	}

	public void setReceiveSms( Boolean receiveSms ) {
		this.receiveSms = receiveSms;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate( Date registrationDate ) {
		this.registrationDate = registrationDate;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName( String userName ) {
		this.userName = userName;
	}

	public Boolean getInfoRestricted() {
		return infoRestricted;
	}

	public Boolean getReceiveEmail() {
		return receiveEmail;
	}

	public Boolean getReceiveSms() {
		return receiveSms;
	}

	public List<IUserProperty> getProperties() {
		if( properties == null ) {
			properties = new ArrayList<RestUserProperty>();
		}

		return ConversionFactory.getUserPropertyConverter().transfer( properties );
	}

	public void setProperties( List<? extends IUserProperty> properties ) {
		this.properties = ConversionFactory.getUserPropertyConverter().transfer( properties, RestUserProperty.class );
	}

	public NotificationType getNotifyUserOnCreation() {
		return notifyUserOnCreation;
	}

	public void setNotifyUserOnCreation( NotificationType notifyUserOnCreation ) {
		this.notifyUserOnCreation = notifyUserOnCreation;
	}
}
