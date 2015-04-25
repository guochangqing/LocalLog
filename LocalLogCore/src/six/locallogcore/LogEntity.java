package six.locallogcore;

public class LogEntity {
	public static final int TYPE_DEBUG = 0;
	public static final int TYPE_WARN = 1;
	public static final int TYPE_ERROR = 2;
	private int type = TYPE_DEBUG;
	private long time = 0;
	private String content = null;
	public LogEntity(int type,long time,String content){
		this.type = type;
		this.time = time;
		this.content = content;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
