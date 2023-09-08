package bankApp.vo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class Member implements Serializable {

	//클래스 변수
	private final int key;
	private final String id;
	private final String name;
	private final String password;
}
