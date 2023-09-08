package bankApp.service;

import bankApp.vo.Board;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import bankApp.utils.BankUtils;

public class BoardService {
	private BoardService() {};
	private static final BoardService boardService = new BoardService();
	MemberService memberService = MemberService.getInstance();
	public static BoardService getInstance() {
		return boardService;
	}
	List<Board> boards = new ArrayList<Board>(); // 게시글 리스트
	{
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("boards.ser"));
			boards = (List<Board>) ois.readObject();
			ois.close();
		} catch (ClassNotFoundException | IOException e1) {	}
	}

	/**
	 * @author YJM
	 * @param 게시판 메뉴
	 * @return 문자, 특수문자 사용 x only 숫자
	 */
	public void boardMain() {
		BoardSearch();
		while (true) {
			try {
				int input = BankUtils.nextInt("=================================================" + "\n"
						+ "\t[1] 글작성 \t[2] 글조회 \t[0] 메인메뉴" + "\n================================================");
				switch (input) {
				case 0: // 종료
					System.out.println("메인 메뉴로 돌아갑니다.");
					return;
				case 1: // 글작성
					writer();
					break;
				case 2: // 글조회
					menu();
					break;
				default:
					System.out.println("!! [1],[2],[0] 알맞은 숫자를 입력하세요. !!");
					break;
				}
			} catch (NumberFormatException e) {
				System.out.println("잘못된 입력입니다. 숫자를 입력하거나 '0'을 입력하여 메인메뉴로 가십시오.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @author YJM
	 * @param 게시판 조회 메뉴
	 * @return 게시글 번호 입력 시 조회.
	 */
	public void menu() {
		while (true) {
			try {
				String input1 = BankUtils.nextLine("글번호를 입력하시면 해당 글로 이동합니다.       b.뒤로가기");
				switch (input1) {
				case "b":
					BoardSearch();
					return;
				default:
					view(Integer.parseInt(input1));
					break;
				}
			} catch (NumberFormatException e) {
				System.out.println("잘못된 입력입니다. 숫자를 입력하거나 'b'를 입력하여 뒤로 가십시오.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * @author YJM
	 * @param 게시글 조회 메서드
	 * @return 게시글 번호 입력받아 있는지 없는지 조회
	 */
	public Board findBy(int no) {
		Board board = null;
		for (int i = 0; i < boards.size(); i++) {
			if (no == boards.get(i).getCnt()) {
				board = boards.get(i);
				break;
			}
		}
		return board;
	}

	/**
	 * @author YJM
	 * @param 게시글 정렬
	 * @return 최신순이 상단으로 가게 하기 위해 버블정렬 (내림차순)
	 */
	public void BoardSearch() {
		List<Board> arr = new ArrayList<>(boards);
		for (int j = 0; j < arr.size() - 1; j++) {
			for (int i = 0; i < arr.size() - 1 - j; i++) {
				if (arr.get(i).getCnt() < arr.get(i + 1).getCnt()) {
					Board tmp = arr.get(i);
					arr.set(i, arr.get(i + 1));
					arr.set(i + 1, tmp);
				}
			}
		}
		System.out.println("========================= S A J O 게시판 =============================");
		System.out.println(" 글번호 작성자    글제목       작성일자       조회수");
		System.out.println("======================================================================");
		for (int i = 0; i < arr.size(); i++) {
			System.out.println(arr.get(i));
		}
	}

	/**
	 * @author YJM
	 * @param 단일 조회
	 * @return 게시글 번호 입력 시 단일 글 조회
	 */
	public void view(int cnt) {
		Board board = findBy(cnt);
		if (board == null) {
			System.out.println("=============================================");
			System.out.println("없는 게시글 번호입니다.");
			System.out.println("=============================================");
			return;
		}

		board.setClick(board.getClick() + 1); // 클릭숫자 늘리기
		BankUtils.outputStream("boards.ser", boards);

		System.out.println("===========================================================");
		System.out.println(String.format("%-6s %15d %-6s %15s", "글번호", board.getCnt(), "작성자", board.getName()));
		System.out.println("===========================================================");
		System.out.println(String.format("%-6s %15s %-6s %10d", "작성일", board.getDate(), "조회수", board.getClick()));
		System.out.println("===========================================================");
		System.out.println(String.format("%-6s %s", "제목", board.getTitle()));
		System.out.println("===========================================================");
//		System.out.println(String.format("%s", board.getContent()));
		String content = board.getContent();
		int maxLineLength = 30; // 한 줄에 출력할 최대 글자 수
		int index = 0;

		while (index < content.length()) {
		    if (index + maxLineLength < content.length()) {
		        System.out.println(content.substring(index, index + maxLineLength));
		        index += maxLineLength;
		    } else {
		        System.out.println(content.substring(index));
		        break;
		    }
		}

		System.out.println("===========================================================");
		

		if (!board.getName().equals(MemberService.getInstance().getLoginUser().getName())) {
			try {
				BankUtils.nextInt("1. 목록");
			} catch (NumberFormatException e) {
				System.out.println("잘못된 입력입니다. 이 글은 목록만 조회 가능합니다. 게시판으로 돌아갑니다.");
			} catch (Exception e) {
				System.out.println("잘못된 입력입니다. 이 글은 목록만 조회 가능합니다. 게시판으로 돌아갑니다.");
			}
			BoardSearch();
			return;
		}
		while (true) {
			try {
				switch (BankUtils.nextInt(("1. 목록(Menu)   2. 수정    3.삭제"))) {
				case 1:
					BoardSearch();
					return;
				case 2:
					modify(board.getCnt());
					BoardSearch();
					return;
				case 3:
					remove(cnt);
					BoardSearch();
					return;
				default:
					System.out.println("'1' '2' '3' 알맞은 숫자를 입력하세요.");
					break;
				}
			} catch (NumberFormatException e) {
				System.out.println("잘못된 입력입니다. 숫자를 입력하거나 '1'을 입력하여 뒤로 가십시오.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @author YJM
	 * @param 게시글 작성
	 * @return ...작성
	 */
	public void writer() {
		int cnt = 1;
		if (boards.size() > 0)
			cnt = (boards.get(boards.size() - 1).getCnt()) + 1;
		String name = MemberService.getInstance().getLoginUser().getName(); // name 은 로그인 유저 이름 고정 // 싱글톤?.
		String title = BankUtils.nextLine("제목을 입력하세요", t -> t.matches("^[A-Za-z0-9-가-힣\\s\\p{ASCII}]{2,}$"),"최소한 2글자 이상의 문자로 구성");
		String content = BankUtils.nextLine("내용을 입력하세요", t -> t.matches("^[A-Za-z0-9-가-힣\\s\\p{ASCII}]{10,}$"),"최소한 10글자 이상의 문자로 구성");

		String date = BankUtils.today();
		boards.add(new Board(cnt, name, title, content, date, 0));
		BoardSearch();
		BankUtils.outputStream("boards.ser", boards);
	}

	/**
	 * @author YJM
	 * @param 게시글 수정
	 * @return 로그인 데이터가 일치할 시 게시글 단일 조회 시 수정 가능.
	 */
	public void modify(int cnt) {
		Board s = findBy(cnt);
		String title = BankUtils.nextLine("제목을 입력하세요", t -> t.matches("^[A-Za-z0-9-가-힣\\s\\p{ASCII}]{2,}$"),"최소한 2글자 이상의 문자로 구성");
		String content = BankUtils.nextLine("내용을 입력하세요", t -> t.matches("^[A-Za-z0-9-가-힣\\s\\p{ASCII}]{10,}$"),"최소한 10글자 이상의 문자로 구성");
		s.setTitle(title);
		s.setContent(content);
		System.out.println(" 게시글 수정이 완료되었습니다 ");
		BankUtils.outputStream("boards.ser", boards);
	}

	/**
	 * @author YJM
	 * @param 게시글 삭제
	 * @return 로그인 데이터가 일치할 시 게시글 단일 조회 시 삭제 가능.
	 */
	public void remove(int cnt) {
		Board board = findBy(cnt);
		boards.remove(board);
		BankUtils.outputStream("boards.ser", boards);
		System.out.println("게시글이 삭제 되었습니다.");
	}
}