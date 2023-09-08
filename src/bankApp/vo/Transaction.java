package bankApp.vo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Transaction implements Serializable {
	/**
	 * @author pyj
	 * @param Field
	 * 
	 * account : 계좌번호 FK
	 * date : 거래 날짜 년도 월 일
	 * type : 입출금
	 * name : 이름
	 * amount : 거래 금액
	 * money : 잔액
	 * time: 거래 시간 시 분 초
	 */
	private String account;
	private String date; 
	private String type; 
	private String name; 
	private int amount; 
	private int money;	
	private String time;
	private int key;
}
