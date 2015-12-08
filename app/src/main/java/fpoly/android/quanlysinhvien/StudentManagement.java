package fpoly.android.quanlysinhvien;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StudentManagement extends ActionBarActivity {
	String Malop;

	ListView lvSV;
	Spinner spnMalop;
	Spinner spnMalop1;
	
	ArrayAdapter<Student> adapter_sv;
	ArrayAdapter<String> adapter_lop;

	ArrayList<Student> dsSV = new ArrayList<Student>();
	ArrayList<Student> dsSV1 = new ArrayList<Student>();
	ArrayList<Lop> dsLop = new ArrayList<Lop>();
	ArrayList<String> dsMalop = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student_management);

		//Dua danh sach Ma lop vao spinner
		spnMalop = (Spinner)findViewById(R.id.spnMaLop);
		showDsLop();
		//Tham chieu den listview
		lvSV = (ListView)findViewById(R.id.lvDs_sinhvien);

		//Them sinh vien
		Button bntThem = (Button)findViewById(R.id.bntThem);
		bntThem.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Tham chieu den dialog
				final Dialog sinhvien = new Dialog(StudentManagement.this);
				sinhvien.setContentView(R.layout.add_student);
				sinhvien.setTitle("Thêm sinh viên");

				//Tham chieu cac control tren dialog
				Button bntXoatrang = (Button) sinhvien.findViewById(R.id.bntXoatrang);
				Button bntThemSV = (Button) sinhvien.findViewById(R.id.bntThemSV);
				spnMalop1 = (Spinner) sinhvien.findViewById(R.id.spnMalop);
				final EditText edtMaSV = (EditText) sinhvien.findViewById(R.id.edtMaSV);
				final EditText edtHoten = (EditText) sinhvien.findViewById(R.id.edtTenSV);
				final EditText edtNgaysinh = (EditText) sinhvien.findViewById(R.id.edtNgaysinh);
				final EditText edtDiachi = (EditText) sinhvien.findViewById(R.id.edtDiachi);
				final EditText edtEmail = (EditText) sinhvien.findViewById(R.id.edtEmail);

				spnMalop1.setAdapter(adapter_lop);

				//Gan gia tri mac dinh cho Ma sinh vien
				if (dsSV.size() > 0) {
					String so = dsSV.get(dsSV.size() - 1).masv;
					int stt = Integer.parseInt(so.substring(2));
					edtMaSV.setText("SV00" + (stt + 1));
				} else
					edtMaSV.setText("SV001");

				//Nut them sinh vien
				bntThemSV.setOnClickListener(new Button.OnClickListener() {
					@Override
					public void onClick(View v) {
						//Lay gi tri cac control tren dialog
						String malop = spnMalop1.getSelectedItem().toString();
						String masv = edtMaSV.getText().toString();
						String hoten = edtHoten.getText().toString();
						String ngaysinh = edtNgaysinh.getText().toString();
						String diachi = edtDiachi.getText().toString();
						String email = edtEmail.getText().toString();

						//Loai bo khoang trang
						hoten = hoten.trim();
						ngaysinh = ngaysinh.trim();
						diachi = diachi.trim();
						email = email.trim();

						//Kiem tra tinh hop le cua du lieu
						if (malop.equalsIgnoreCase("")) {
							Toast.makeText(StudentManagement.this, "Bạn chưa chọn Mã lớp", Toast.LENGTH_LONG).show();
						} else if (hoten.equalsIgnoreCase("")) {
							Toast.makeText(StudentManagement.this, "Bạn chưa nhập Họ tên", Toast.LENGTH_LONG).show();
						} else if (ngaysinh.equalsIgnoreCase("")) {
							Toast.makeText(StudentManagement.this, "Bạn chưa nhập Ngày sinh", Toast.LENGTH_LONG).show();
						} else if (kiemtra_ngaythang(ngaysinh) == false) {
							Toast.makeText(StudentManagement.this, "Ngày sinh có dạng: dd/mm/yyyy", Toast.LENGTH_LONG).show();
						} else if (diachi.equalsIgnoreCase("")) {
							Toast.makeText(StudentManagement.this, "Bạn chưa nhập Địa chỉ", Toast.LENGTH_LONG).show();
						} else if (email.equalsIgnoreCase("")) {
							Toast.makeText(StudentManagement.this, "Bạn chưa nhập Email", Toast.LENGTH_LONG).show();
						} else if (kiemtra_Email(email) == false) {
							Toast.makeText(StudentManagement.this, "Email không hợp lệ", Toast.LENGTH_LONG).show();
						}
						//Khi du lieu da hop le
						else {
							//Thay the khoang trang tim duoc trong chuoi
							hoten = hoten.replaceAll("\\s+", " ");
							diachi = diachi.replaceAll("\\s+", " ");
							ngaysinh = ngaysinh.replaceAll("\\s+", "");
							email = email.replaceAll("\\s+", "");

							//Student b = new Student(malop, masv, hoten, ngaysinh, diachi, email);
							ParseObject dtSV = new ParseObject("Sinhvien");
							dtSV.put("Malop", malop);
							dtSV.put("MaSV", masv);
							dtSV.put("Hoten", hoten);
							dtSV.put("Ngaysinh", ngaysinh);
							dtSV.put("Diachi", diachi);
							dtSV.put("Email", email);
							dtSV.saveInBackground(new SaveCallback() {
								@Override
								public void done(com.parse.ParseException e) {
									//Khi viec them thanh cong
									if (e == null) {
										Toast.makeText(StudentManagement.this, "Đã thêm thành công", Toast.LENGTH_LONG).show();

										showDsSVwithMalop();
									}
									//Khi them du lieu that bai
									else {
										Toast.makeText(StudentManagement.this, e.getMessage(), Toast.LENGTH_LONG).show();
									}
								}
							});

							sinhvien.dismiss();
						}
					}
				});

				//Nut xoa trang tren dialog Them sinh vien
				bntXoatrang.setOnClickListener(new Button.OnClickListener() {

					@Override
					public void onClick(View v) {

						spnMalop1.setSelection(0);
						edtHoten.setText("");
						edtNgaysinh.setText("");
						edtDiachi.setText("");
						edtEmail.setText("");
						edtHoten.requestFocus();
					}
				});
				sinhvien.show();
			}
		});

		//Su kien click tren listview
		lvSV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

				//Lay ra thong tin sinh vien duoc chon
				final Student a = dsSV1.get(position);

				//Tham chieu den dialog
				final Dialog diaSV = new Dialog(StudentManagement.this);
				diaSV.setTitle("Cập nhật sinh viên");
				diaSV.setContentView(R.layout.update_delete_sv);

				//Tham chieu den cac control tren dialog
				spnMalop1 = (Spinner) diaSV.findViewById(R.id.spnMalop);
				spnMalop1.setAdapter(adapter_lop);

				final EditText edtMaSV = (EditText) diaSV.findViewById(R.id.edtMaSV);
				final EditText edtHoten = (EditText) diaSV.findViewById(R.id.edtTenSV);
				final EditText edtNgaysinh = (EditText) diaSV.findViewById(R.id.edtNgaysinh);
				final EditText edtDiachi = (EditText) diaSV.findViewById(R.id.edtDiachi);
				final EditText edtEmail = (EditText) diaSV.findViewById(R.id.edtEmail);
				final Button bntCapnhat = (Button) diaSV.findViewById(R.id.bntCapnhat);
				final Button bntXoa = (Button) diaSV.findViewById(R.id.bntXoa);

				//Hien thi gia tri mac dinh len spinner
				int vt = adapter_lop.getPosition(a.malop);
				spnMalop1.setSelection(vt);

				//Gan gia tri len cac control tren dialog
				edtMaSV.setText(a.masv);
				edtHoten.setText(a.hoten);
				edtNgaysinh.setText(a.ngaysinh);
				edtDiachi.setText(a.diachi);
				edtEmail.setText(a.email);

				//Nhan nut Cap nhat tren dialog
				bntCapnhat.setOnClickListener(new Button.OnClickListener() {
					@Override
					public void onClick(View v) {
						//Lay gia tri cac control tren dialog
						String malop = spnMalop1.getSelectedItem().toString();
						String masv = edtMaSV.getText().toString();
						String hoten = edtHoten.getText().toString();
						String ngaysinh = edtNgaysinh.getText().toString();
						String diachi = edtDiachi.getText().toString();
						String email = edtEmail.getText().toString();

						//Loai bo khoang trang
						hoten = hoten.trim();
						ngaysinh = ngaysinh.trim();
						diachi = diachi.trim();
						email = email.trim();

						//Kiem tra tinh hop le cua du lieu
						if (malop.equalsIgnoreCase("")) {
							Toast.makeText(StudentManagement.this, "Bạn chưa chọn Mã lớp", Toast.LENGTH_LONG).show();
						} else if (hoten.equalsIgnoreCase("")) {
							Toast.makeText(StudentManagement.this, "Bạn chưa nhập Họ tên", Toast.LENGTH_LONG).show();
						} else if (ngaysinh.equalsIgnoreCase("")) {
							Toast.makeText(StudentManagement.this, "Bạn chưa nhập Ngày sinh", Toast.LENGTH_LONG).show();
						} else if (kiemtra_ngaythang(ngaysinh) == false) {
							Toast.makeText(StudentManagement.this, "Ngày sinh có dạng: dd/mm/yyyy", Toast.LENGTH_LONG).show();
						} else if (diachi.equalsIgnoreCase("")) {
							Toast.makeText(StudentManagement.this, "Bạn chưa nhập Địa chỉ", Toast.LENGTH_LONG).show();
						} else if (email.equalsIgnoreCase("")) {
							Toast.makeText(StudentManagement.this, "Bạn chưa nhập Email", Toast.LENGTH_LONG).show();
						} else if (kiemtra_Email(email) == false) {
							Toast.makeText(StudentManagement.this, "Email không hợp lệ", Toast.LENGTH_LONG).show();
						}
						//Khi du lieu da hop le
						else {
							//Thay the khoang trang tim duoc trong chuoi
							hoten = hoten.replaceAll("\\s+", " ");
							diachi = diachi.replaceAll("\\s+", " ");
							ngaysinh = ngaysinh.replaceAll("\\s+", "");
							email = email.replaceAll("\\s+", "");

							//Cap nhat thong tin cho Lop a
							a.malop = malop;
							a.hoten = hoten;
							a.ngaysinh = ngaysinh;
							a.diachi = diachi;
							a.email = email;

							//Ket noi toi bang Sinh vien tren dam may
							ParseQuery<ParseObject> query = ParseQuery.getQuery("Sinhvien");

							// Lay ra dong du lieu co objectID cu the
							query.getInBackground(a.objectID, new GetCallback<ParseObject>() {
								@Override

								public void done(ParseObject parseObject, com.parse.ParseException e) {
									//Khi lay dong du lieu thanh cong
									if (e == null) {
										//Cap nhat du lieu cho dong du lieu da lay ra
										parseObject.put("Malop", a.malop);
										parseObject.put("Hoten", a.hoten);
										parseObject.put("Ngaysinh", a.ngaysinh);
										parseObject.put("Diachi", a.diachi);
										parseObject.put("Email", a.email);
										parseObject.saveInBackground();

										Toast.makeText(StudentManagement.this,
												"Cập nhật thành công",
												Toast.LENGTH_LONG).show();
										showDsSVwithMalop();

										//Hien thong bao khi truy van bi loi
									} else {
										Toast.makeText(StudentManagement.this,
												e.getMessage(),
												Toast.LENGTH_LONG).show();
									}
								}

							});

							diaSV.dismiss();
						}

					}
				});
				//Nhan nut xoa tren dialog
				bntXoa.setOnClickListener(new Button.OnClickListener() {
					@Override
					public void onClick(View v) {

						//Ket noi den bang Sinhvien tren dam may
						ParseQuery<ParseObject> query = ParseQuery.getQuery("Sinhvien");

						//Lay ra dong du lieu co objectID tuong ung
						query.getInBackground(a.objectID, new GetCallback<ParseObject>() {
							@Override
							public void done(ParseObject parseObject, com.parse.ParseException e) {

								//Truy van du lieu thanh cong
								if (e == null) {
									//Thuc hien xoa dong du lieu da truy van duoc
									parseObject.deleteInBackground(new DeleteCallback() {
										@Override
										public void done(com.parse.ParseException e) {

											//Khi xoa dong du lieu thanh cong
											if (e == null) {
												Toast.makeText(StudentManagement.this,
														"Xóa thành công",
														Toast.LENGTH_LONG).show();
												showDsSVwithMalop();

												//Hien thi thong bao khi xoa du lieu bi loi
											} else {
												Toast.makeText(StudentManagement.this,
														e.getMessage(),
														Toast.LENGTH_LONG).show();
											}
										}
									});
								}
							}
						});

						diaSV.dismiss();
					}
				});
				diaSV.show();

			}
		});

		spnMalop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Malop = spnMalop.getSelectedItem().toString();
				showDsSVwithMalop();

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		showDsSV();
	}

	//Hien thi danh sach sinh vien
	public void showDsSV() {

		//Xoa trang danh sach Sinh vien
		dsSV.clear();

		//Ket noi toi bang Sinhvien tren Parse
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Sinhvien");

		//Lay ra tat ca dong du lieu trong bang Sinhvien
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> list, com.parse.ParseException e) {
				//Khi truy van thanh cong
				if (e == null) {
					//Dua tat ca dong du lieu vao mang dsSV
					for (ParseObject l : list) {
						Student a = new Student();

						a.objectID = l.getObjectId();
						a.malop=l.getString("Malop");
						a.masv = l.getString("MaSV");
						a.hoten = l.getString("Hoten");
						a.ngaysinh = l.getString("Ngaysinh");
						a.diachi = l.getString("Diachi");
						a.email = l.getString("Email");

						dsSV.add(a);
					}
					//Dua danh sach Sinh vien vao ArrayAdapter Sinh vien
					adapter_sv = new ArrayAdapter<Student>(StudentManagement.this, android.R.layout.simple_list_item_1, dsSV);

					//Gan ArrayAdapter Sinh vien vao listview
					lvSV.setAdapter(adapter_sv);
					//Khi truy van that bai
				} else {
					Toast.makeText(StudentManagement.this, e.getMessage(), Toast.LENGTH_LONG).show();
				}

			}

		});

	}

	//Hien thi danh sach sinh vien theo Ma lop
	public void showDsSVwithMalop() {

		//Xoa trang danh sach Sinh vien
		dsSV1.clear();
		//Ket noi toi bang Sinhvien tren Parse
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Sinhvien");
		query.whereEqualTo("Malop", Malop);
		//Lay ra tat ca dong du lieu trong bang Sinhvien
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> list, com.parse.ParseException e) {
				//Khi truy van thanh cong
				if (e == null) {
					//Dua tat ca dong du lieu vao mang dsSV1
					for (ParseObject l : list) {
						Student a = new Student();

						a.objectID = l.getObjectId();
						a.malop=l.getString("Malop");
						a.masv = l.getString("MaSV");
						a.hoten = l.getString("Hoten");
						a.ngaysinh = l.getString("Ngaysinh");
						a.diachi = l.getString("Diachi");
						a.email = l.getString("Email");

						dsSV1.add(a);
					}
					//Dua danh sach Sinh vien vao ArrayAdapter Sinh vien
					adapter_sv = new ArrayAdapter<Student>(StudentManagement.this, android.R.layout.simple_list_item_1, dsSV1);

					//Gan ArrayAdapter Sinh vien vao listview
					lvSV.setAdapter(adapter_sv);
					//Khi truy van that bai
				} else {
					Toast.makeText(StudentManagement.this, e.getMessage(), Toast.LENGTH_LONG).show();
				}

			}

		});

	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.student_management, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.QuanlySV) {
			Intent i = new Intent(StudentManagement.this, Home.class);
			startActivity(i);
			this.finish();
			return true;
		}
		if (id == R.id.Timkiem) {
			Intent i = new Intent(StudentManagement.this, SearchStudent.class);
			startActivity(i);
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	//Kiem tra dinh dang ngay thang nam
	public static boolean kiemtra_ngaythang(String mdate){
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date testDate = null;
		try {
			testDate = sdf.parse(mdate);
		}
		catch (Exception e) {
			return false;
		}
		if (!sdf.format(testDate).equals(mdate)) {
			return false;
		}
		return true;
	}
	//Kiem tra tinh hop le cua email
	public static boolean kiemtra_Email(String email) {
	    Pattern pattern;
	    Matcher matcher;
	    String EMAIL_PATTERN = "^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
	    
	    pattern = Pattern.compile(EMAIL_PATTERN);
	    matcher = pattern.matcher(email);
	    
	    return matcher.matches();
	}

	//Lay danh sach lop tu bang Lop, sau do trich ra danh sach Ma lop
	public void showDsLop() {
		//Xoa trang danh sach lop
		dsLop.clear();
		dsMalop.clear();
		//Ket noi toi bang Lop tren Parse
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Lop");
		//query.whereEqualTo("playerName", "Dan Stemkoski");

		//Lay ra tat ca dong du lieu trong bang Lop
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> list, com.parse.ParseException e) {
				//Khi truy van thanh cong
				if (e == null) {
					//Dua tat ca dong du lieu vao mang dsLop
					for (ParseObject l : list) {
						Lop a = new Lop();

						a.objectID=l.getObjectId();
						a.malop = l.getString("Malop");
						a.tenlop = l.getString("Tenlop");
						a.khoahoc = l.getInt("Khoahoc");

						dsLop.add(a);

					}
					//Lay ra danh sach Ma lop

					for(Lop a:dsLop){
						dsMalop.add(a.malop);
					}
					//Dua danh sach ma lop vao combobox
					adapter_lop = new ArrayAdapter<String>(StudentManagement.this, android.R.layout.simple_spinner_item, dsMalop);
					adapter_lop.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spnMalop.setAdapter(adapter_lop);

					//Khi truy van that bai
				} else {
					Toast.makeText(StudentManagement.this, e.getMessage(), Toast.LENGTH_LONG).show();
				}

			}
		});
	}
}
