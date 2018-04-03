import org.apache.commons.lang.StringUtils;

public class StringMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// StringUtils.reverse(str)
		System.out.println(reverse1("huang a lin"));
	}

	public static String reverse(String str) {
		if (str == null) {
			return null;
		}
		return new StringBuffer(str).reverse().toString();
	}

	public static String reverse1(String str) {
		if (str == null) {
			return null;
		}
		StringBuffer buffer = new StringBuffer();
		char[] arr = str.toCharArray();
		for (int i = arr.length - 1; i >= 0; i--) {
			buffer.append(arr[i]);
		}
		return buffer.toString();
	}
}
