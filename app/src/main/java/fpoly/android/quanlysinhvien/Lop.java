package fpoly.android.quanlysinhvien;

public class Lop {
	String objectID;
	String malop;
	String tenlop;
	int khoahoc;
	
	public Lop(){
		
	}
	public Lop(String malop, String tenlop, int khoahoc) {
		this.malop = malop;
		this.tenlop = tenlop;
		this.khoahoc = khoahoc;
	}
	public String toString(){
		return "Mã lớp: " + malop + "\t Tên lớp: " + tenlop + "\t Khóa học: " + khoahoc;
	}
	
	
}
