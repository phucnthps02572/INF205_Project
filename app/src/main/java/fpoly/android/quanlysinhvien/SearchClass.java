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
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class SearchClass extends ActionBarActivity implements TextWatcher {
	AutoCompleteTextView edtTim;
	ListView lvTim;
	StudentHandler db;
	ArrayAdapter<Lop> adapter_tim;
	ArrayList<Lop> dsTim = new ArrayList<Lop>();
	ArrayList<Lop> dsLop = new ArrayList<Lop>();
	ArrayList<String> dsTenLop=new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_class);

		//Tham chieu den AutoCompleteTextView
		edtTim =(AutoCompleteTextView)findViewById(R.id.edtTimkiem);
		//Dua danh sach lop vao AutoTextview
		showDsLop();

		//Tham chieu den listview
		lvTim = (ListView) findViewById(R.id.lvKetqua);

		//Su kien click tren listview
		lvTim.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				//Lay ra doi tuong Lop duoc chon tren listview
				final Lop a = dsTim.get(position);

				//Tham chieu den dialog Cap nhat lop
				final Dialog diaLop = new Dialog(SearchClass.this);
				diaLop.setTitle("Cập nhật lớp");
				diaLop.setContentView(R.layout.update_delete_lop);

				//Tham chieu den cac control tren dialog
				final TextView txtMalop = (TextView) diaLop.findViewById(R.id.txtMalop);
				final EditText edtTenlop = (EditText) diaLop.findViewById(R.id.edtTenlop);
				final EditText edtKhoahoc = (EditText) diaLop.findViewById(R.id.edtKhoahoc);
				final Button bntCapnhat = (Button) diaLop.findViewById(R.id.bntCapnhat);
				final Button bntXoa = (Button) diaLop.findViewById(R.id.bntXoa);

				//Gan gia tri cua doi tuong lop duoc chon len cac control tren dialog
				txtMalop.setText(a.malop);
				edtTenlop.setText(a.tenlop);
				edtKhoahoc.setText(String.valueOf(a.khoahoc));

				//Click nut Cap nhat tren dialog
				bntCapnhat.setOnClickListener(new Button.OnClickListener() {
					@Override
					public void onClick(View v) {

						//Lay gia tri cac control tren dialog
						String malop = txtMalop.getText().toString();
						String tenlop = edtTenlop.getText().toString();
						String khoa_hoc = edtKhoahoc.getText().toString();
						tenlop = tenlop.trim();
						khoa_hoc = khoa_hoc.trim();

						//Kiem tra tinh hop le cua du lieu
						if (tenlop.equalsIgnoreCase("")) {
							Toast.makeText(SearchClass.this, "Vui lòng nhập Tên lớp", Toast.LENGTH_LONG).show();
						} else if (khoa_hoc.equalsIgnoreCase(""))
							Toast.makeText(SearchClass.this, "Vui lòng nhập Khóa học", Toast.LENGTH_LONG).show();

						else {
							try {
								int khoahoc = Integer.parseInt(khoa_hoc);
								if (khoahoc <= 0) {
									Toast.makeText(SearchClass.this, "Khóa học là số lớn hơn 0", Toast.LENGTH_LONG).show();
								} else {
									//Khi du lieu hop le thi gan vao doi tuong Lop a
									tenlop = tenlop.replaceAll("\\s+", " ");
									a.malop = malop;
									a.tenlop = tenlop;
									a.khoahoc = khoahoc;

									//Ket noi toi bang Lop tren dam may
									ParseQuery<ParseObject> query = ParseQuery.getQuery("Lop");

									// Lay ra dong du lieu co objectID cu the
									query.getInBackground(a.objectID, new GetCallback<ParseObject>() {
										@Override

										public void done(ParseObject parseObject, com.parse.ParseException e) {
											//Khi lay dong du lieu thanh cong
											if (e == null) {
												//Cap nhat du lieu cho dong du lieu da lay ra
												parseObject.put("Malop", a.malop);
												parseObject.put("Tenlop", a.tenlop);
												parseObject.put("Khoahoc", a.khoahoc);
												parseObject.saveInBackground();

												Toast.makeText(SearchClass.this,
														"Cập nhật thành công",
														Toast.LENGTH_LONG).show();

												showDsTim();
												showDsLop();

												//Hien thong bao khi truy van bi loi
											} else {
												Toast.makeText(SearchClass.this,
														e.getMessage(),
														Toast.LENGTH_LONG).show();
											}
										}

									});
									diaLop.dismiss();
								}
							} catch (Exception ex) {
								Toast.makeText(SearchClass.this, "Khóa học là số nguyên dương", Toast.LENGTH_LONG).show();
							}

						}
					}

				});
				//Click nut xoa tren dialog Cap nhat lop
				bntXoa.setOnClickListener(new Button.OnClickListener() {
					@Override
					public void onClick(View v) {

						//Ket noi den bang Lop tren dam may
						ParseQuery<ParseObject> query = ParseQuery.getQuery("Lop");

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

												Toast.makeText(SearchClass.this, "Đã xóa thành công", Toast.LENGTH_LONG).show();
												//edtTim.setText("");
												showDsTim();
												showDsLop();

												//Hien thi thong bao khi xoa du lieu bi loi
											} else {
												Toast.makeText(SearchClass.this,
														e.getMessage(),
														Toast.LENGTH_LONG).show();
											}
										}
									});
								}
							}
						});
						diaLop.dismiss();
					}
				});
				diaLop.show();
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_class, menu);
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

	//Lay danh sach lop tu bang Lop
	public void showDsLop() {
		//Xoa trang danh sach lop
		dsLop.clear();

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
					//Khi truy van that bai
				} else {
					Toast.makeText(SearchClass.this, e.getMessage(), Toast.LENGTH_LONG).show();
				}
				//Lay ra danh sach Ten lop
				for(int i=0; i<dsLop.size(); i++){
					dsTenLop.add(dsLop.get(i).tenlop);
				}
				edtTim.addTextChangedListener(SearchClass.this);

				//Dua danh sach Ten lop vao AutoCompleteTextView
				edtTim.setAdapter(
						new ArrayAdapter<String>
								(SearchClass.this, android.R.layout.simple_list_item_1, dsTenLop));
			}
		});
	}
	//Hien thi danh sach tim kiem
	public void showDsTim(){
		//Xoa trang dsTim
		dsTim.clear();

		//Lay ra chuoi can tim kiem
		String strTim = edtTim.getText().toString();

		//Lay danh sach Lop can tim theo Ten lop tu Parse
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Lop");
		query.whereContains("Tenlop", strTim);

		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> scoreList, ParseException e) {
				if (e == null) {
					//Truy van du lieu tu Parse thanh cong
					for (ParseObject l : scoreList) {
						Lop a = new Lop();

						a.objectID = l.getObjectId();
						a.malop = l.getString("Malop");
						a.tenlop = l.getString("Tenlop");
						a.khoahoc = l.getInt("Khoahoc");

						dsTim.add(a);

					}
					//Tham chieu den Listview
					lvTim = (ListView)findViewById(R.id.lvKetqua);
					//Gan danh sach Lop tim duoc vao ArrayAdapter
					adapter_tim = new ArrayAdapter<Lop>(SearchClass.this, android.R.layout.simple_list_item_1,dsTim);

					lvTim.setAdapter(adapter_tim);
					//Hien thi thong bao khi khong tim thay
					if(dsTim.size()==0){
						Toast.makeText(SearchClass.this, "Không tìm thấy", Toast.LENGTH_LONG).show();
					}

				}
				//Truy van du lieu tu Parse that bai
				else {
					Toast.makeText(SearchClass.this, e.getMessage(), Toast.LENGTH_LONG).show();
				}
			}
		});


	}
	//Hien thi danh sach Lop da tim kiem theo Ten lop
	public void Timkiem_lop(View v){
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
