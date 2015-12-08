package fpoly.android.quanlysinhvien;

import java.util.ArrayList;
import java.util.List;

import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class SearchStudent extends ActionBarActivity implements TextWatcher {
	AutoCompleteTextView edtTim;

	ArrayList<Student> dsTim = new ArrayList<Student>();
	ArrayAdapter<Student> adapter_tim;
	ArrayAdapter<String> adapter_lop;
	ListView lvTim;
	Spinner spnMalop1;
	ArrayList<Lop> dsLop = new ArrayList<Lop>();
	ArrayList<Student> dsSV = new ArrayList<Student>();
	ArrayList<String> dsMalop = new ArrayList<String>();
	ArrayList<String> dsTenSV = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_student);

		edtTim =(AutoCompleteTextView)findViewById(R.id.edtTimkiem);
		//Dua danh sach ten sinh vien vao AutoTextview
		showDsSV();
		//Lay ra danh sach Malop
		showDsLop();

		//Tham chieu den listview
		lvTim = (ListView)findViewById(R.id.lvKetqua);

		//Su kien click tren listview
		lvTim.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				//Tham chieu den dialog
				final Student a = dsTim.get(position);
				final Dialog diaSV = new Dialog(SearchStudent.this);
				diaSV.setTitle("Cập nhật sinh viên");
				diaSV.setContentView(R.layout.update_delete_sv);

				//Tham chieu den spinner
				spnMalop1 = (Spinner)diaSV.findViewById(R.id.spnMalop);				

				//Gan danh sach Malop len spinner
				adapter_lop = new ArrayAdapter<String>(SearchStudent.this, android.R.layout.simple_spinner_item, dsMalop);
				adapter_lop.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spnMalop1.setAdapter(adapter_lop);

				//Tham chieu den cac control tren dialog
				final EditText edtMaSV = (EditText) diaSV.findViewById(R.id.edtMaSV);
				final EditText edtHoten = (EditText) diaSV.findViewById(R.id.edtTenSV);
				final EditText edtNgaysinh = (EditText) diaSV.findViewById(R.id.edtNgaysinh);
				final EditText edtDiachi = (EditText) diaSV.findViewById(R.id.edtDiachi);
				final EditText edtEmail = (EditText) diaSV.findViewById(R.id.edtEmail);
				final Button bntCapnhat = (Button) diaSV.findViewById(R.id.bntCapnhat);
				final Button bntXoa = (Button) diaSV.findViewById(R.id.bntXoa);

				//Gan gia tri mac dinh cho spinner
				int vt = adapter_lop.getPosition(a.malop);
				spnMalop1.setSelection(vt);

				//Gan gia tri cho cac control
				edtMaSV.setText(a.masv);
				edtHoten.setText(a.hoten);
				edtNgaysinh.setText(a.ngaysinh);
				edtDiachi.setText(a.diachi);
				edtEmail.setText(a.email);

				//Nhap nut Cap nhat tren dialog
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

						//Xoa khoang trang trong chuoi
						hoten=hoten.trim();
						ngaysinh=ngaysinh.trim();
						diachi=diachi.trim();
						email=email.trim();

						//Kiem tra tinh hop le cua du lieu
						if(malop.equalsIgnoreCase("")){
							Toast.makeText(SearchStudent.this, "Bạn chưa chọn Mã lớp", Toast.LENGTH_LONG).show();
						}
						else if(hoten.equalsIgnoreCase("")){
							Toast.makeText(SearchStudent.this, "Bạn chưa nhập Họ tên", Toast.LENGTH_LONG).show();
						}
						else if(ngaysinh.equalsIgnoreCase("")){
							Toast.makeText(SearchStudent.this, "Bạn chưa nhập Ngày sinh", Toast.LENGTH_LONG).show();
						}
						else if(StudentManagement.kiemtra_ngaythang(ngaysinh)==false){
							Toast.makeText(SearchStudent.this, "Ngày sinh có dạng: dd/mm/yyyy", Toast.LENGTH_LONG).show();
						}
						else if(diachi.equalsIgnoreCase("")){
							Toast.makeText(SearchStudent.this, "Bạn chưa nhập Địa chỉ", Toast.LENGTH_LONG).show();
						}
						else if(email.equalsIgnoreCase("")){
							Toast.makeText(SearchStudent.this, "Bạn chưa nhập Email", Toast.LENGTH_LONG).show();
						}
						else if(StudentManagement.kiemtra_Email(email)==false){
							Toast.makeText(SearchStudent.this, "Email không hợp lệ", Toast.LENGTH_LONG).show();
						}
						//Khi du lieu hop le
						else{
							hoten=hoten.replaceAll("\\s+", " ");
							diachi=diachi.replaceAll("\\s+", " ");
							ngaysinh=ngaysinh.replaceAll("\\s+", "");
							email=email.replaceAll("\\s+", "");

							//Dua du lieu cap nhat vao doi tuong Student a
							a.malop = malop;
							a.hoten = hoten;
							a.ngaysinh = ngaysinh;
							a.diachi = diachi;
							a.email = email;

							//Ket noi toi bang Lop tren dam may
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

										Toast.makeText(SearchStudent.this,
												"Cập nhật thành công",
												Toast.LENGTH_LONG).show();

										showDsTim();
										showDsSV();

										//Hien thong bao khi truy van bi loi
									} else {
										Toast.makeText(SearchStudent.this,
												e.getMessage(),
												Toast.LENGTH_LONG).show();
									}
								}

							});


							diaSV.dismiss();
						}
						
						
					}
				});
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

												Toast.makeText(SearchStudent.this, "Đã xóa thành công", Toast.LENGTH_LONG).show();
												//edtTim.setText("");
												showDsTim();
												showDsSV();

												//Hien thi thong bao khi xoa du lieu bi loi
											} else {
												Toast.makeText(SearchStudent.this,
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
		
	}

	//Hien thi danh sach sinh vien
	public void showDsSV() {

		//Xoa trang danh sach Sinh vien
		dsSV.clear();
		dsTenSV.clear();
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
						a.malop = l.getString("Malop");
						a.masv = l.getString("MaSV");
						a.hoten = l.getString("Hoten");
						a.ngaysinh = l.getString("Ngaysinh");
						a.diachi = l.getString("Diachi");
						a.email = l.getString("Email");

						dsSV.add(a);
					}
					//Lay ra danh sach Ten sinh vien
					for(int i=0; i<dsSV.size(); i++){
						dsTenSV.add(dsSV.get(i).hoten);
					}
					edtTim.addTextChangedListener(SearchStudent.this);

					//Dua danh sach Ten sinh vien vao AutoCompleteTextView
					edtTim.setAdapter(
							new ArrayAdapter<String>
									(SearchStudent.this, android.R.layout.simple_list_item_1, dsTenSV));
					//Khi truy van that bai
				} else {
					Toast.makeText(SearchStudent.this, e.getMessage(), Toast.LENGTH_LONG).show();
				}

			}

		});

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
					//Khi truy van that bai
				} else {
					Toast.makeText(SearchStudent.this, e.getMessage(), Toast.LENGTH_LONG).show();
				}

			}
		});
	}

	//Hien thi danh sach tim kiem
	public void showDsTim(){
		//Xoa trang dsTim
		dsTim.clear();

		//Lay ra chuoi can tim kiem
		String strTim = edtTim.getText().toString();

		//Lay danh sach Sinh vien can tim theo Ten sinh vien tu Parse
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Sinhvien");
		query.whereContains("Hoten", strTim);

		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> scoreList, ParseException e) {
				if (e == null) {
					//Truy van du lieu tu Parse thanh cong
					for (ParseObject l : scoreList) {
						Student a = new Student();

						a.objectID = l.getObjectId();
						a.malop = l.getString("Malop");
						a.masv=l.getString("MaSV");
						a.hoten=l.getString("Hoten");
						a.ngaysinh=l.getString("Ngaysinh");
						a.diachi=l.getString("Diachi");
						a.email=l.getString("Email");

						dsTim.add(a);

					}
					//Tham chieu den Listview
					lvTim = (ListView)findViewById(R.id.lvKetqua);

					//Gan danh sach Sinh vien tim duoc vao ArrayAdapter
					adapter_tim = new ArrayAdapter<Student>(SearchStudent.this, android.R.layout.simple_list_item_1,dsTim);

					lvTim.setAdapter(adapter_tim);
					//Hien thi thong bao khi khong tim thay
					if(dsTim.size()==0){
						Toast.makeText(SearchStudent.this, "Không tìm thấy", Toast.LENGTH_LONG).show();
					}

				}
				//Truy van du lieu tu Parse that bai
				else {
					Toast.makeText(SearchStudent.this, e.getMessage(), Toast.LENGTH_LONG).show();
				}
			}
		});


	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_student, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.QuanlySV) {
			Intent i = new Intent(this, Home.class);
			startActivity(i);
			finish();
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	public void Timkiem_SV(View v){		
		showDsTim();
		
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}
}
