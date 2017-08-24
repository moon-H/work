package cn.unicompay.wallet.client.framework.api.http.model;

public class VerifyWoSecurityQuestionRq
{
	private String woAccountId;
	private String question;
	private String answer;
	
	
	public String getWoAccountId()
	{
		return woAccountId;
	}
	public void setWoAccountId(String woAccountId)
	{
		this.woAccountId = woAccountId;
	}
	public String getQuestion()
	{
		return question;
	}
	public void setQuestion(String question)
	{
		this.question = question;
	}
	public String getAnswer()
	{
		return answer;
	}
	public void setAnswer(String answer)
	{
		this.answer = answer;
	}
}
