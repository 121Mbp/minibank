package bankApp.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import bankApp.utils.BankUtils;
import bankApp.vo.Transaction;

public class TransactionService {
	/**
	 * 
	 * @author pjy, kwon
	 * @param 계좌 내역 조회 서비스
	 * @return AccountService 인스턴스
	 */
	private TransactionService() {};
	private static final TransactionService transactionService = new TransactionService();
	public static TransactionService getInstance() {
		return transactionService;
	}
	
	List<Transaction> transactions = new ArrayList<>();

	{
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("transactions.ser"));
			transactions = (List<Transaction>) ois.readObject();
			ois.close();
		} catch (ClassNotFoundException | IOException e) {
//			e.printStackTrace();
		}
	}
	
	public void transactionMain() {
		AccountService accountService = AccountService.getInstance();
		int cnt = 0;
		int accountLength = accountService.accountList.size();
		if(transactions.size() == 0) {
			System.out.println("거래내역이 없습니다.");
			return;
		}
		System.out.println("===================");
		for (int i = 0; i < accountLength; i++) {
			System.out.println("[" + (i+1) + "] " + accountService.accountList.get(i).getAccount());
		}
		System.out.println("===================");	
		int list = BankUtils.nextInt("조회할 계좌를 선택해 주세요. > ");		
		
		if (list > 0 && list <= accountLength) {
			System.out.println("============================== 조회 내역 =============================================");
			System.out.println("거래 일자    거래타입     예금주     거래 금액          잔액           거래 일시");
			System.out.println("====================================================================================");
			for (int j = 0; j < transactions.size(); j++) {
				if(accountService.accountList.get(list-1).getAccount().equals(transactions.get(j).getAccount())) {
					cnt++;
					System.out.println(String.format("%10s %6s %8s %15s %15s %16s", 
						transactions.get(j).getDate(), 
						transactions.get(j).getType(),
						transactions.get(j).getName(),
						transactions.get(j).getAmount() + "원", 
						transactions.get(j).getMoney() + "원",
						transactions.get(j).getTime())
					);
				}
			}	
			if(cnt == 0) {
				System.out.println("거래내역이 없습니다.");
			}
		} else {
			System.out.println("잘못 입력하였습니다. 메인 서비스 메뉴로 이동합니다. > ");
		}
	}			
}

