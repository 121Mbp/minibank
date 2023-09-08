package bankApp.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import bankApp.utils.BankUtils;
import bankApp.vo.Account;
import bankApp.vo.Member;
import bankApp.vo.Transaction;

public class AccountService {
	/**
	 * 
	 * @author kwon
	 * @param 계좌 서비스
	 * @return MemberService, TransactionService, BoardService 인스턴스
	 */
	MemberService memberService = MemberService.getInstance();
	TransactionService transactionService = TransactionService.getInstance();
	BoardService boardService = BoardService.getInstance();
	
	private AccountService() {};
	private static final AccountService accountService = new AccountService();
	public static AccountService getInstance() {
		return accountService;
	}
	
	List<Account> accounts = new ArrayList<>();
	List<Account> accountList = new ArrayList<>();
	
	{
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("accounts.ser"));
			accounts = (List<Account>) ois.readObject();
			ois.close();
		} catch (FileNotFoundException e) {
			//accounts.add(new Account("12-0000", 0, false, 0, ""));
		} catch (ClassNotFoundException | IOException e) {
//			e.printStackTrace();
		}
	}
	
	/**
	 * @author kwon
	 * @param 계좌서비스 메인
	 */
	Member user = null; 
	public void accountMain() {
		user = memberService.getLoginUser(); // 로그인 유저 정보
		
		while(true) {
			accountList.clear();
			for (int i = 0; i < accounts.size(); i++) {
				if(user.getKey() == accounts.get(i).getKey()) {
					accountList.add(accounts.get(i));
				}
			}
			try {
				int input = BankUtils.nextInt("\n서비스 메뉴를 선택해주세요 > "
						+ "\n========================================================================================\n" 
						+ "[1] 계좌개설 [2] 입금 [3] 출금 [4] 계좌이체 [5] 거래내역조회 [6] 게시판 [7] 로그아웃"
						+ "\n========================================================================================");
				
				switch (input) {
					case 1: // 계좌개설
						System.out.println("계좌개설 서비스 입니다.");
						accountRegister();
						break;
					case 2: // 입금
						System.out.println("입금 서비스 입니다.");
						accountDeposit();
						break;
					case 3: // 출금
						System.out.println("출금 서비스 입니다.");
						accountWithdraw();
						break;
					case 4: // 계좌이체
						System.out.println("계좌이체 서비스 입니다.");
						accountTransfer();
						break;
					case 5: // 거래내역조회
						System.out.println("거래내역조회 서비스 입니다.");
						transactionService.transactionMain();
						break;
					case 6: // 게시판
						boardService.boardMain();
						break;
					case 7: // 로그아웃
						System.out.println(user.getName() +"님 로그아웃 되었습니다.");
						user = null;
						return;
					default:
						System.out.println("잘못 입력하였습니다. 다시 입력해 주세요. > ");
						break;
				}
			} catch (NumberFormatException e) {
				System.out.println("숫자로 입력하세요.");
			} catch (Exception e) {
//				//
			}
		}
	}
	
	/**
	 * @author kwon
	 * @param 계좌 등록
	 * @return 계좌 3개 까지 개설 가능
	 */
	public void accountRegister() {
//		System.out.println(accountList);
		if(accountList.size() >= 3) {
			System.out.println("계좌는 3개이상 개설하실 수 없습니다. \n메인 서비스 메뉴로 이동합니다. >");
			return;	
		}
		System.out.println("새로운 계좌를 개설하시겠습니까?");
		int flag = BankUtils.nextInt("===================\n"
				+ "[1] 예  [2] 아니오" +
				"\n===================");
		switch (flag) {
			case 1:
				accountCreate();
				break;
			case 2:
				System.out.println("계좌 개설을 취소하였습니다. \n메인 서비스 메뉴로 이동합니다. >");
				return;
			default:
				System.out.println("잘못 입력하였습니다. 다시 입력해 주세요. > ");
				break;
		}
	}
	
	/**
	 * @author kwon
	 * @param 계좌 생성
	 * @return 4자리 랜덤숫자 중복 불가 
	 */
	public void accountCreate() {
		while(true) {
			String number = (int)(Math.random() * 9000) + 1000 + ""; // 1000 ~ 9999
			boolean first = false;
			for (int i = 0; i < accounts.size(); i++) {
				if(number == accounts.get(i).getAccount().split("-")[1]) return;
			}
			System.out.println("신규 계좌 개설이 완료 되었습니다.");
			System.out.println("-----------------------");
			System.out.println(user.getName() + "님의 신규 계좌 정보");
			System.out.println("계좌번호: 12-" + number);
			String date = BankUtils.today();
			System.out.println("계좌개설일: " + date);
			System.out.println("-----------------------");
			if(accountList.size() == 0) first = true;
			accounts.add(new Account("12-" + number, 0, first, user.getKey(), date));
			BankUtils.outputStream("accounts.ser", accounts);
			return;
		}
	}
		
	/**
	 * @author kwon
	 * @param 입금
	 */
	public void accountDeposit() {
		if(accountList.size() == 0) {
			System.out.println("계좌가 없습니다. 계좌를 개설해 주세요. \n메인 서비스 메뉴로 이동합니다. > ");
			return;
		}
		accountList(); // 통장 리스트
		int list = BankUtils.nextInt("거래할 계좌를 선택해 주세요. > ");
		if(list == 0) {
			mainAccountChange();
		} else if (list > 0 && list <= accountList.size()) {
			int saving = accountList.get(list-1).getMoney();
			int money = Math.abs(BankUtils.nextInt("입금할 금액을 입력하세요. > "));
			if(money == 0) {
				System.out.println("입금할 금액을 다시 확인해 주세요. 메인 서비스 메뉴로 이동합니다. > ");
				return;
			}
			saving += money;
			accountList.get(list-1).setMoney(saving);
			System.out.println("입금이 완료 되었습니다.");
			String date = BankUtils.today();
			String time = BankUtils.currentTime();
			accountBalance(list, date); // 통장잔액
			transactionService.transactions.add(new Transaction(accountList.get(list-1).getAccount(), date, "입금", user.getName(), money, saving, time, user.getKey()));
			BankUtils.outputStream("transactions.ser", transactionService.transactions);
		} else {
			System.out.println("잘못 입력하였습니다. 메인 서비스 메뉴로 이동합니다. > ");
		}
	}
	
	/**
	 * @author kwon
	 * @param 출금
	 * @return 비밀번호 오류 3회 초과, 잔액 부족 시 
	 */
	public void accountWithdraw() {
		if(accountList.size() == 0) {
			System.out.println("계좌가 없습니다. 계좌를 개설해 주세요. \n메인 서비스 메뉴로 이동합니다. > ");
			return;
		}
		accountList(); // 통장 리스트
		int list = BankUtils.nextInt("거래할 계좌를 선택해 주세요. > ");
		if(list == 0) {
			mainAccountChange();
		} else if (list > 0 && list <= accountList.size()) {
			int money = Math.abs(BankUtils.nextInt("출금할 금액을 입력하세요. >"));
			if(money == 0) {
				System.out.println("출금할 금액을 다시 확인해 주세요. 메인 서비스 메뉴로 이동합니다. > ");
				return;
			}
			String password = BankUtils.nextLine("비밀번호를 입력하세요 >", pass->pass.equals(user.getPassword()), "비밀번호를 잘못 입력하였습니다.");
			if(password.equals("3")) {
				System.out.println("비밀번호 오류 3회 초과입니다. 메인 서비스 메뉴로 이동합니다. > ");
				return;
			}
			int saving = accountList.get(list-1).getMoney();
			if(saving < money) {
				System.out.println("잔액이 부족합니다. 메인 서비스 메뉴로 이동합니다. > ");
				return;
			}
			saving -= money;
			accountList.get(list-1).setMoney(saving);
			System.out.println("출금이 완료 되었습니다.");
			String date = BankUtils.today();
			String time = BankUtils.currentTime();
//			System.out.println(time);
			accountBalance(list, date); // 통장잔액
			transactionService.transactions.add(new Transaction(accountList.get(list-1).getAccount(), date, "출금", user.getName(), money, saving, time, user.getKey()));
			BankUtils.outputStream("transactions.ser", transactionService.transactions);
		} else {
			System.out.println("잘못 입력하였습니다. 메인 서비스 메뉴로 이동합니다. > ");
		}
	}
	
	/**
	 * @author kwon
	 * @param 계좌이체
	 * @return 계좌번호 불일치 시, 비밀번호 오류 3회 초과, 잔액 부족 시 
	 */
	public void accountTransfer() {
		if(accountList.size() == 0) {
			System.out.println("계좌가 없습니다. 계좌를 개설해 주세요. \n메인 서비스 메뉴로 이동합니다. > ");
			return;
		}
		accountList(); // 통장 리스트
		Account receiveKey = null;
		Member receive = null;
		int list = BankUtils.nextInt("거래할 계좌를 선택해 주세요. > ");
		String receiveAccount = BankUtils.nextLine("보낼 통장의 계좌번호를 입력하세요. > ");
		for (int i = 0; i < accounts.size(); i++) {
			if(accounts.get(i).getAccount().equals(receiveAccount)) {
				receiveKey = accounts.get(i);
			}
		}
		for (int i = 0; i < memberService.members.size(); i++) {
			if(memberService.members.get(i).getKey() == receiveKey.getKey()) {
				receive = memberService.members.get(i);
			}
		}
		if(receive.getName() == null) {
			System.out.println("잘못된 계좌번호입니다. 메인 서비스 메뉴로 이동합니다. >");
			return;
		}
		int money = Math.abs(BankUtils.nextInt("보낼 금액을 입력하세요. >"));
		if(money == 0) {
			System.out.println("출금할 금액을 다시 확인해 주세요. 메인 서비스 메뉴로 이동합니다. > ");
			return;
		}
		String password = BankUtils.nextLine("비밀번호를 입력하세요 >", pass->pass.equals(user.getPassword()), "비밀번호를 잘못 입력하였습니다.");
		if(password.equals("3")) {
			System.out.println("비밀번호 오류 3회 초과입니다. 메인 서비스 메뉴로 이동합니다. > ");
			return;
		}
		int saving = accountList.get(list-1).getMoney();
		if(saving < money) {
			System.out.println("잔액이 부족합니다. 메인 서비스 메뉴로 이동합니다. > ");
			return;
		}
		int flag = BankUtils.nextInt(receive.getName() + "님에게 "+ money + "원을 송금하시겠습니까?\n" 
				+ "===================\n"
				+ "[1] 예  [2] 아니오" +
				"\n===================");
		switch (flag) {
		case 1:
			saving -= money;
			accountList.get(list-1).setMoney(saving);
			System.out.println("이체가 완료 되었습니다.");
			String date = BankUtils.today();
			String time = BankUtils.currentTime();
			accountBalance(list, date); // 통장잔액
			transactionService.transactions.add(new Transaction(accountList.get(list-1).getAccount(), date, "출금", receive.getName(), money, saving, time, user.getKey()));
			transactionService.transactions.add(new Transaction(receiveAccount, date, "입금", user.getName(), money, receiveKey.getMoney() + money, time, receive.getKey()));
			BankUtils.outputStream("transactions.ser", transactionService.transactions);
			break;
		case 2:
			System.out.println("송금을 취소하였습니다. 메인 서비스 메뉴로 이동합니다. >");
			return;
		default:
			System.out.println("잘못 입력하였습니다. 다시 입력해 주세요. > ");
			break;
		}
	}
	
	/**
	 * @author kwon
	 * @param 통장리스트 ft.주거래 통장 변경
	 */
	public void accountList() {
		int primaryKey = 0;
		System.out.println("===================");
		for (int i = 0; i < accountList.size(); i++) {
			if(accountList.get(i).isMainAccount()) {
				primaryKey = i+1;
				System.out.println("[" + (i+1) + "] " + accountList.get(i).getAccount()+ " *");
			} else {
				System.out.println("[" + (i+1) + "] " + accountList.get(i).getAccount());
			}
		}
		System.out.println("[0] 주거래 통장 변경");
		System.out.println("===================");
		System.out.println(primaryKey + "번 계좌가 주거래 통장으로 설정되어 있습니다.");
	}
	
	/**
	 * @author kwon
	 * @param 주거래 통장 변경
	 * @return boolean 
	 */
	public void mainAccountChange() {
		int change = BankUtils.nextInt("주거래 통장으로 사용할 계좌를 선택해 주세요. > ");
		for (int i = 0; i < accountList.size(); i++) {
			accountList.get(i).setMainAccount(false);
			if(i == change-1) accountList.get(i).setMainAccount(true);
		}
		System.out.println("주거래 통장 변경이 완료 되었습니다. 메인 서비스 메뉴로 이동합니다. > ");
	}
	
	/**
	 * @author kwon
	 * @param 통장잔액
	 * @return list, date
	 */
	public void accountBalance(int list, String date) {
		System.out.println("--------------------");
		System.out.println("계좌번호: " + accountList.get(list-1).getAccount());
		System.out.println("잔액: " + accountList.get(list-1).getMoney() + "원");
		System.out.println("거래일시: " + date);
		System.out.println("--------------------");
	}
}
