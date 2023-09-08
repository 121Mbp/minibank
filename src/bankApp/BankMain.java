package bankApp;

import bankApp.service.MemberService;

public class BankMain {
	public static void main(String[] args) {
		System.out.println("\n" + 
		"     _____ ___       ______     ____  ___    _   ____ __\n" + 
		"    / ___//   |     / / __ \\   / __ )/   |  / | / / //_/\n" + 
		"    \\__ \\/ /| |__  / / / / /  / __  / /| | /  |/ / ,<   \n" + 
		"   ___/ / ___ / /_/ / /_/ /  / /_/ / ___ |/ /|  / /| |  \n" + 
		"  /____/_/  |_\\____/\\____/  /_____/_/  |_/_/ |_/_/ |_|  \n");

		MemberService memberService = MemberService.getInstance();
		memberService.memberMain();
	}
}
