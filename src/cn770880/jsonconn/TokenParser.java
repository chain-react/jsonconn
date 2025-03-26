package cn770880.jsonconn;

public interface TokenParser {
	/**
	 * 建议做法：如果没有传token，那就返回0，代表游客<br/>
	 * 如果有token，但是token失效了，请抛出异常:throw new SvrException(SvrException.TOKEN_INVALID_ERROR, "token失效错误 ! ")<br/>
	 * @param token
	 * @return
	 */
	public long parseToken(String token);
}
