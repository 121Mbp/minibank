package bankApp.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bankApp.vo.Account;

public class BankUtils {
	private static Scanner scanner = new Scanner(System.in);
	private static SimpleDateFormat simpleDateFormat;
	
	/**
	 * @param string msg
	 */
	public static String nextLine(String msg) {
		System.out.println(msg);
		return scanner.nextLine();
	}
	
	/**
	 * @param int msg
	 */
	public static int nextInt(String msg) {
		return Integer.parseInt(nextLine(msg));
	}
	
	/**
	 * @param str
	 * @return 이름은 한글 2-4자로 구성 (이름) (Student 예제의 chkHangle 변형)
	 */
	public static Boolean nameRule(String str) {
		Pattern pattern = Pattern.compile("^[가-힣]{2,4}$");
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
	
	/**
	 * @param str
	 * @return 비밀번호는 영어 대/소문자, 숫자 4-12자로 구성 (비밀번호)
	 */
	public static Boolean passwordRule(String str) {
		Pattern pattern = Pattern.compile("^[A-Za-z0-9]{4,12}$");
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
	
	/**
	 * @param str
	 * @return 아이디는 영어 소문자, 숫자 3-12자로 구성 (아이디)
	 */
	public static Boolean idRule(String str) {
		Pattern pattern = Pattern.compile("^[a-z0-9]{3,12}$");
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
	
	/**
	 * @param msg, pred, errorMsg
	 * @return str
	 */
	public static String nextLine(String msg, Predicate<String> pred, String errorMsg) {
		int err = 0;
		while(true) {
			String str = nextLine(msg);
			if(!pred.test(str)) {
				err++;
				System.out.println(errorMsg);
				if(err > 2) break;
			} else {
				return str;
			}				
		}
		return err + "";
	}
	
	/**
	 * @param 날짜 
	 * @return date
	 */
	public static String today() {
		simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
		String date = simpleDateFormat.format(Calendar.getInstance().getTime());
		return date;
	}

	/**
	 * @param 시간 
	 * @return time
	 */
	public static String currentTime() {
		simpleDateFormat = new SimpleDateFormat("HH시 mm분 ss초");
		String time = simpleDateFormat.format(Calendar.getInstance().getTime());
		return time;
	}
	
	/**
	 * @param 요일 
	 * @return date
	 */
	public static String dayOfWeek() {
		simpleDateFormat = new SimpleDateFormat("오늘은 E요일입니다.");
		String date = simpleDateFormat.format(Calendar.getInstance().getTime());
		return date;
	}

	/**
	 * @author kwon
	 * @param 출력 스트림
	 */
	public static Map<String, List<?>> outputStream(String path, List arr) {
		Map<String, List> map = new HashMap<String, List>();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
			oos.writeObject(arr);
			oos.flush();
			oos.close();
		} catch (IOException e) {
			// TODO: handle exception
		}
		return (Map<String, List<?>>) (map.put(path, arr));
		
	}
}
