
public class DArrayMain {

	public static int n = 0;// 数组中所有元素的个数  
    public static boolean[][] flag = new boolean[4][4];// 标记数组，记录该元素有没有输出  
  
    public static void main(String[] args) {  
        // 这里为了方便就先用了一个固定的二维数组  
        int[][] intArr = { { 1, 2, 3, 4 }, { 5, 6, 7, 8 }, { 9, 10, 11, 12 },  
                { 13, 14, 15, 16 } };  
        int i, j;  
        for (i = 0; i < flag.length; i++) {  
            for (j = 0; j < flag[i].length; j++) {  
                flag[i][j] = false;  
            }  
        }  
//        function(intArr, 0, 0, 0);// 最后的两个元素代表要打印元素的下标  
        ScrewArray(intArr, 4, 4);
    }  
  
    private static void function(int[][] intArr, int k, int a, int b) {  
        // TODO Auto-generated method stub  
        // System.out.println("\n" + n + " " + k + " " + a + " " +b);  
        if (n == 16) {// 该二维数组共16个元素  
            return;  
        }  
        k %= 4;  
        int i = 0;  
        switch (k) {  
        case 0:// 向右输出  
            for (i = b; i < intArr[a].length; i++) {  
                if (flag[a][i]) {  
                    break;  
                }  
                flag[a][i] = true;  
                System.out.print(intArr[a][i] + " ");  
                n++;  
                b = i;  
                System.out.println("b="+b);
            }  
            a += 1;// 为下一次打印做准备  
            break;  
        case 1:// 向下输出  
            for (i = a; i < intArr.length; i++) {  
                if (flag[i][b]) {  
                    break;  
                }  
                flag[i][b] = true;  
                System.out.print(intArr[i][b] + " ");  
                n++;  
                a = i;  
            }  
            b -= 1;// 为下一次打印做准备  
            break;  
        case 2:// 向左输出  
            for (i = b; i >= 0; i--) {  
                if (flag[a][i]) {  
                    break;  
                }  
                flag[a][i] = true;  
                System.out.print(intArr[a][i] + " ");  
                n++;  
                b = i;  
            }  
            a -= 1;// 为下一次打印做准备  
            break;  
        case 3:// 向上输出  
            for (i = a; i >= 0; i--) {  
                if (flag[i][b]) {  
                    break;  
                }  
                flag[i][b] = true;  
                System.out.print(intArr[i][b] + " ");  
                n++;  
                a = i;  
            }  
            b += 1;// 为下一次打印做准备  
            break;  
        default:  
            System.out.print("error!");  
            break;  
        }  
        // System.out.println("\n" + n + " " + k + " " + a + " " +b);  
        function(intArr, k + 1, a, b);  
    }  
    
    public static void ScrewArray(int[][] arr,int i,int j) {
			int left=0,right=j-1,first=0,last=i-1;
			while (left<right || first<last) {
				for (int k = left; k <=right; k++) {
					System.out.print(arr[first][k]);
				}
				System.out.println();
				first++;
				for (int k = first; k <=last; k++) {
					System.out.print(arr[k][right]);
				}
				System.out.println();
				right--;
				for (int k = right; k >=left; k--) {
					System.out.print(arr[last][k]);
				}
				System.out.println();
				last--;
				for (int k = last; k >=first; k--) {
					System.out.print(arr[k][left]);
				}
				System.out.println();
				left++;
			}
	}
}
