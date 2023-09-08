package bankApp.service;

import static bankApp.utils.BankUtils.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import bankApp.utils.BankUtils;
import bankApp.vo.Member;
import lombok.Data;
import lombok.Getter;

@Data
public class MemberService {
	private Member member;
	@Getter
	private Member loginUser;
	private Member duplicateId;

	private MemberService() {
	};

	private static final MemberService memberService = new MemberService();

	public static MemberService getInstance() {
		return memberService;
	}
	List<Member> members = new ArrayList<Member>();
	/**
	 * @author GHL
	 * @param members.ser 파일 불러오기
	 */
	{
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("members.ser"));
			members = (List<Member>) ois.readObject();
			ois.close();
		} catch (ClassNotFoundException | IOException e1) {
		}
	}


	/**
	 * @author ghl
	 * @param 멤버 메인
	 */
	public void memberMain() {
		while (true) {
			try {
				int input = BankUtils.nextInt(
						"=========================================================\n" + "\t[1] 로그인 \t[2] 회원가입 \t[0] 종료"
								+ "\n=========================================================");
				switch (input) {
				case 0: // 종료
					System.out.println("프로그램이 종료되었습니다.");
					return;
				case 1: // 로그인
					memberLogin();
					loginUser = AccountService.getInstance().user;
					break;
				case 2: // 회원가입
					memberRegister();
					break;
				case 3:
					memberList();
					break;
				default:
					System.out.println("잘못 입력하였습니다. 다시 입력해 주세요. > ");
					break;
				}
			} catch (NumberFormatException e) {
				System.out.println("숫자로 입력하세요.");
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @author ghl
	 * @param 멤버 회원가입
	 */
	public void memberRegister() {
		String id = nextLine("아이디를 입력하세요. > ");
		if (!idRule(id)) {
			System.out.println("아이디는 영어 소문자, 숫자 3-12자로 입력해주세요.");
			return;
		}
		for (int i = 0; i < members.size(); i++) {
			if (members.get(i).getId().equals(id)) {
				duplicateId = members.get(i);
			}
		}
		if (duplicateId != null) {
			System.out.println("이미 사용된 아이디 입니다.");
			duplicateId = null;
			return;
		}
		String name = nextLine("이름을 입력하세요. > ");
		if (!nameRule(name)) {
			System.out.println("이름은 한글 2-4자로 입력해주세요");
			return;
		}
			
		String password = nextLine("비밀번호를 입력하세요. > ");
		if (!passwordRule(password)) {
			System.out.println("비밀번호는 영어 대소문자, 숫자 4-12자로 입력해주세요.");
			return;
		}
		String rePassword = nextLine("비밀번호를 재입력하세요. > ");
		if (!password.equals(rePassword)) {
			System.out.println("비밀번호가 일치하지 않습니다. \n처음으로 돌아갑니다.");
			return;
		}
		int key = (int) (System.currentTimeMillis() / 2000);
		members.add(Member.builder().key(key).id(id).name(name).password(password).build());
		BankUtils.outputStream("members.ser", members);
		System.out.println(name + "님 가입을 축하드립니다. \n잠시 후 로그인 하시면 뱅킹 서비스를 이용하실 수 있습니다.");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}



	/**
	 * @author ghl
	 * @param 멤버 로그인
	 */
	public void memberLogin() {
		String id = nextLine("아이디를 입력하세요. > ");
		if (!idRule(id)) {
			System.out.println("아이디는 영어 소문자, 숫자 3-12자로 입력해주세요.");
			return;
		}
		String password = nextLine("비밀번호를 입력하세요. > ");
		if (!passwordRule(password)) {
			System.out.println("비밀번호는 4~12자의 영문 대/소문자, 숫자를 사용해 주세요.");
		}
		for (int i = 0; i < members.size(); i++) {
			if (members.get(i).getId().equals(id) && members.get(i).getPassword().equals(password)) {
				loginUser = members.get(i);
				System.out.print(loginUser.getName() + "님 반갑습니다. ");
				dayOfWeek();
			}
		}
		if (loginUser == null) {
			System.out.println("로그인에 실패하였습니다.");
			return;
		}
		AccountService.getInstance().accountMain();
		loginUser = null;
		return;

	}
	
	/**
	 * @author ghl
	 * memberList()
	 */
	public void memberList() {
		for (int i = 0; i < members.size(); i++) {
			System.out.println(members.get(i));

		}

	}


}
