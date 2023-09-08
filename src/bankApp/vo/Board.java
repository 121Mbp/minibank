package bankApp.vo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@AllArgsConstructor
@Data
@Builder
public class Board implements Serializable {
	private int cnt;
	private String name;
	private String title;
	private String content;
	private String date;
	private int click;

	public String toString() {
		return String.format("%5d   %.3s  %.5s  %.0s  %10s %8d", cnt, name, title, content, date, click);
	}
}
