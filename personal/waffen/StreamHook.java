


public class StreamHook extends Stream {

	public StreamHook(byte[] var1) {
		super(var1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void writeString(String var1) {
		WriteStringEvent writeString = new WriteStringEvent(var1);
		EventManager.callEvent(writeString);
		super.writeString(writeString.str);
	}
}
