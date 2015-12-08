package fpoly.android.quanlysinhvien;

public class Student {
	String objectID;
	String malop;
	String masv;
	String hoten;
	String ngaysinh;
	String diachi;
	String email;
	
	public Student() {
		
	}
	public Student(String malop, String masv, String hoten, String ngaysinh, String diachi, String email){
		this.malop = malop;
		this.masv = masv;
		this.hoten = hoten;
		this.ngaysinh = ngaysinh;
		this.diachi = diachi;
		this.email = email;
	}
	
	public String toString(){
		return  "Mã lớp: " + malop + "\n" + 
				"MaSV: " + masv + "\n" +
				"Họ tên: " + hoten + "\n" +
				"Ngày sinh: " + ngaysinh + "\n" +
				"Địa chỉ: " + diachi + "\n" + 
				"Email: " + email;
				
	}
}
