package no.sumo.api.entity.sumo;

/**
 * ProgramTree entity.
 * 
 * @author Thomas
 */
//@SecondaryTables(
//      {@SecondaryTable (name="PROGRAMS", schema="VMAN"),
//      @SecondaryTable(name="PROGRAMS_PUBLISHED", schema="VMAN")}
//)
public class ProgramViewCount implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 6315880421897491987L;

	private Long programId;

	private Long viewCount;

	// Constructors

	/** default constructor */
	public ProgramViewCount() {
	}

	/** full constructor */
	public ProgramViewCount( Long programId, Long viewCount ) {
		this.programId = programId;
		this.viewCount = viewCount;
	}

	public Long getProgramId() {
		return programId;
	}

	public void setProgramId( Long programId ) {
		this.programId = programId;
	}

	public Long getViewCount() {
		return viewCount;
	}

	public void setViewCount( Long viewCount ) {
		if( viewCount == null ) {
			viewCount = 0L;
		}
		this.viewCount = viewCount;
	}

}