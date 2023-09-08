package bankApp.vo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Account implements Serializable{
	/**
	 * @author kwon
	 * @param Field
	 * 
	 * account : 계좌번호
	 * money : 계좌에 있는 돈 (0 ~ 21억 숫자)
	 * mainAccount : 주계좌 여부 ( True or False )
	 * account : 계좌번호 	
	 * key : FK
	 */
	private String account;
	private int money;
	private boolean mainAccount;
	private int key;
	private String date;
}
