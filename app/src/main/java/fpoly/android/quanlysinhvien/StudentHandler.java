package fpoly.android.quanlysinhvien;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class StudentHandler extends SQLiteOpenHelper {

	private static final String ID = "Malop";
	private static final String TENLOP = "Tenlop";
	private static final String KHOAHOC = "Khoahoc";
	private static final String TABLE_LOP = "Lop";

	private static final String MALOP = "Malop";
	private static final String MASV = "MaSV";
	private static final String HOTEN = "Hoten";
	private static final String NGAYSINH = "Ngaysinh";
	private static final String DIACHI = "Diachi";
	private static final String EMAIL = "Email";
	private static final String TABLE_SV = "Sinhvien";

	public StudentHandler(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOP);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SV);

		//Tao bang LOP
		String CREATE_TABLE = "CREATE TABLE " + TABLE_LOP + "(" + ID
				+ " TEXT PRIMARY KEY," + TENLOP + " TEXT,"
				+ KHOAHOC + " INTEGER)";

		db.execSQL(CREATE_TABLE);
		
		//Them du lieu vao bang LOP
		db.execSQL("INSERT INTO " + TABLE_LOP + " VALUES('L01', 'Lập trình di động', 10)");
		db.execSQL("INSERT INTO " + TABLE_LOP + " VALUES('L02', 'Ứng dụng phần mềm', 10)");
		db.execSQL("INSERT INTO " + TABLE_LOP + " VALUES('L03', 'Thiết kế đồ họa', 10)");
		db.execSQL("INSERT INTO " + TABLE_LOP + " VALUES('L04', 'Lập trình di động', 11)");
		db.execSQL("INSERT INTO " + TABLE_LOP + " VALUES('L05', 'Ứng dụng phần mềm', 11)");

		//Tao bang SINHVIEN
		CREATE_TABLE = "CREATE TABLE " + TABLE_SV + "(" + MALOP + " TEXT,"
				+ MASV + " TEXT PRIMARY KEY," + HOTEN
				+ " TEXT," + NGAYSINH + " TEXT," + DIACHI + " TEXT," + EMAIL
				+ " TEXT)";
		db.execSQL(CREATE_TABLE);
		
		//Them du lieu vao bang SINHVIEN
		db.execSQL("INSERT INTO " + TABLE_SV + " VALUES('L01', 'SV001', 'Lê Nguyễn Thùy Linh', '30/09/1993',"
				+ "'CMT8, Quận Tân Bình, TPHCM', 'thuylinh@gmail.com')");
		db.execSQL("INSERT INTO " + TABLE_SV + " VALUES('L02', 'SV002', 'Đoàn Duy Bình', '11/11/1991',"
				+ "'Nguyễn Trãi, Quận 5, TPHCM', 'duybinh@gmail.com')");
		db.execSQL("INSERT INTO " + TABLE_SV + " VALUES('L03', 'SV003', 'Đào Huy Tuấn', '10/10/1995',"
				+ "'Nguyễn Duy Trinh, Quận 2, TPHCM', 'huytuan@gmail.com')");
		db.execSQL("INSERT INTO " + TABLE_SV + " VALUES('L04', 'SV004', 'Trần Quốc Công', '09/10/1995',"
				+ "'Trường Chinh, Quận Tân Bình, TPHCM', 'quoccong@gmail.com')");
		db.execSQL("INSERT INTO " + TABLE_SV + " VALUES('L05', 'SV005', 'Nguyễn Tấn Ngọc', '28/10/1993',"
				+ "'Hai Bà Trưng, Quận 3, TPHCM', 'tanngoc@gmail.com')");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOP);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SV);
		onCreate(db);
	}

	public ArrayList<Lop> danhsachLop() {
		ArrayList<Lop> dsLop = new ArrayList<Lop>();
		String selectQuery = "SELECT * FROM " + TABLE_LOP;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				String malop = cursor.getString(0);
				String tenlop = cursor.getString(1);
				int khoahoc = Integer.parseInt(cursor.getString(2));
				Lop a = new Lop(malop, tenlop, khoahoc);
				dsLop.add(a);
			} while (cursor.moveToNext());
		}
		return dsLop;
	}
	
	public void themLop(Lop a){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues value = new ContentValues();
		value.put(ID, a.malop);
		value.put(TENLOP, a.tenlop);
		value.put(KHOAHOC, a.khoahoc);		
		db.insert(TABLE_LOP, null, value);		
		db.close();
		
	}
	
	public void xoaLop(Lop a){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_LOP, ID + " = ?",new String[]{String.valueOf(a.malop)});
	}
	
	public int capnhatLop(Lop a){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues value = new ContentValues();
		value.put(ID,a.malop);
		value.put(TENLOP, a.tenlop);
		value.put(KHOAHOC, a.khoahoc);
		
		return db.update(TABLE_LOP, value, ID + " = ?", new String[]{String.valueOf(a.malop)});
	}
	
	public ArrayList<Lop> timkiemLop(String strTenlop){		
		ArrayList<Lop> dsLop = new ArrayList<Lop>();		
		String selectQuery = "SELECT * FROM "+ TABLE_LOP + " WHERE Tenlop like '%" + strTenlop + "%'";		
		SQLiteDatabase db = this.getWritableDatabase();		
		Cursor cursor = db.rawQuery(selectQuery, null);		
		if(cursor.moveToFirst()){
			do{
				String malop = cursor.getString(0);
				String tenlop = cursor.getString(1);
				int khoahoc = Integer.parseInt(cursor.getString(2));
				Lop a = new Lop(malop, tenlop, khoahoc);
				dsLop.add(a);				
			}while(cursor.moveToNext()); 			
		}
		return dsLop;
	}
	
	public ArrayList<Student> danhsachSV() {
		ArrayList<Student> dsSV = new ArrayList<Student>();
		String selectQuery = "SELECT * FROM " + TABLE_SV;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				String malop = cursor.getString(0);
				String masv = cursor.getString(1);
				String hoten = cursor.getString(2);
				String ngaysinh = cursor.getString(3);
				String diachi = cursor.getString(4);
				String email = cursor.getString(5);
				Student sv = new Student(malop, masv, hoten, ngaysinh, diachi, email);
				dsSV.add(sv);
			} while (cursor.moveToNext());
		}
		return dsSV;
	}
	
	public void themSV(Student sv){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues value = new ContentValues();
		value.put(MALOP, sv.malop);
		value.put(MASV, sv.masv);
		value.put(HOTEN, sv.hoten);
		value.put(NGAYSINH, sv.ngaysinh);
		value.put(DIACHI, sv.diachi);
		value.put(EMAIL, sv.email);
		db.insert(TABLE_SV, null, value);		
		db.close();
		
	}
	
	public void xoaSV(Student sv){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_SV, MASV + " = ?", new String[]{String.valueOf(sv.masv)});
	}
	
	public int capnhatSV(Student sv){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues value = new ContentValues();
		value.put(MALOP, sv.malop);
		value.put(MASV, sv.masv);
		value.put(HOTEN, sv.hoten);
		value.put(NGAYSINH, sv.ngaysinh);
		value.put(DIACHI, sv.diachi);
		value.put(EMAIL, sv.email);		
		return db.update(TABLE_SV, value, MASV + " = ?", new String[]{String.valueOf(sv.masv)});
	}
	
	public ArrayList<Student> timkiemSV(String strTenSV){		
		ArrayList<Student> dsSV = new ArrayList<Student>();		
		String selectQuery = "SELECT * FROM "+ TABLE_SV + " WHERE Hoten like '%" + strTenSV + "%'";		
		SQLiteDatabase db = this.getWritableDatabase();		
		Cursor cursor = db.rawQuery(selectQuery, null);		
		if(cursor.moveToFirst()){
			do{
				String malop = cursor.getString(0);
				String masv = cursor.getString(1);
				String hoten = cursor.getString(2);
				String ngaysinh = cursor.getString(3);
				String diachi = cursor.getString(4);
				String email = cursor.getString(5);
				Student sv = new Student(malop, masv, hoten, ngaysinh, diachi, email);
				dsSV.add(sv);			
			}while(cursor.moveToNext()); 			
		}
		return dsSV;
	}
	
	public ArrayList<Student> timkiemSV_malop(String strMalop){		
		ArrayList<Student> dsSV = new ArrayList<Student>();		
		String selectQuery = "SELECT * FROM "+ TABLE_SV + " WHERE Malop = '" + strMalop + "'";		
		SQLiteDatabase db = this.getWritableDatabase();		
		Cursor cursor = db.rawQuery(selectQuery, null);		
		if(cursor.moveToFirst()){
			do{
				String malop = cursor.getString(0);
				String masv = cursor.getString(1);
				String hoten = cursor.getString(2);
				String ngaysinh = cursor.getString(3);
				String diachi = cursor.getString(4);
				String email = cursor.getString(5);
				Student sv = new Student(malop, masv, hoten, ngaysinh, diachi, email);
				dsSV.add(sv);			
			}while(cursor.moveToNext()); 			
		}
		return dsSV;
	}
}
