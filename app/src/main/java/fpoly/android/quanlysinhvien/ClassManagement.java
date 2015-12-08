package fpoly.android.quanlysinhvien;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import android.support.v7.app.ActionBarActivity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public class ClassManagement extends ActionBarActivity {
	ListView lvLop;
	StudentHandler db;
	ArrayAdapter_Lop adapter_lop;
	ArrayList<Lop> dsLop = new ArrayList<Lop>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_class_management);

		lvLop = (ListView) findViewById(R.id.lv_Dslop);

		//su kien click tren listview
		lvLop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				final Lop a = dsLop.get(position);

				//Tham chieu den dialog
				final Dialog diaLop = new Dialog(ClassManagement.this);
				diaLop.setTitle("Cập nhật lớp");
				diaLop.setContentView(R.layout.update_delete_lop);

				//Tham chieu cac control tren dialog
				final TextView txtMalop = (TextView) diaLop.findViewById(R.id.txtMalop);
				final EditText edtTenlop = (EditText) diaLop.findViewById(R.id.edtTenlop);
				final EditText edtKhoahoc = (EditText) diaLop.findViewById(R.id.edtKhoahoc);
				final Button bntCapnhat = (Button) diaLop.findViewById(R.id.bntCapnhat);
				final Button bntXoa = (Button) diaLop.findViewById(R.id.bntXoa);

				//Gan gia tri len cac control tren dialog
				txtMalop.setText(a.malop);
				edtTenlop.setText(a.tenlop);
				edtKhoahoc.setText(String.valueOf(a.khoahoc));

				//Click nut Cap nhat tren dialog
				bntCapnhat.setOnClickListener(new Button.OnClickListener() {
					@Override
					public void onClick(View v) {

						//Lay gia tri tu cac control tren dialog
						String malop = txtMalop.getText().toString();
						String tenlop = edtTenlop.getText().toString();
						String khoa_hoc = edtKhoahoc.getText().toString();
						tenlop = tenlop.trim();
						khoa_hoc = khoa_hoc.trim();

						//Kiem tra tinh hop le cua du lieu
						if (tenlop.equalsIgnoreCase("")) {
							Toast.makeText(ClassManagement.this, "Vui lòng nhập Tên lớp", Toast.LENGTH_LONG).show();
						} else if (khoa_hoc.equalsIgnoreCase(""))
							Toast.makeText(ClassManagement.this, "Vui lòng nhập Khóa học", Toast.LENGTH_LONG).show();
						else {
							try {
								int khoahoc = Integer.parseInt(khoa_hoc);
								if (khoahoc <= 0) {
									Toast.makeText(ClassManagement.this, "Khóa học là số lớn hơn 0", Toast.LENGTH_LONG).show();
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

												Toast.makeText(ClassManagement.this,
														"Cập nhật thành công",
														Toast.LENGTH_LONG).show();
												showDsLop();

												//Hien thong bao khi truy van bi loi
											} else {
												Toast.makeText(ClassManagement.this,
														e.getMessage(),
														Toast.LENGTH_LONG).show();
											}
										}

									});

									diaLop.dismiss();
								}
							} catch (Exception ex) {
								Toast.makeText(ClassManagement.this, "Khóa học là số nguyên dương", Toast.LENGTH_LONG).show();
							}
						}

					}
				});

				//Click nut Xoa tren dialog
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
												Toast.makeText(ClassManagement.this,
														"Xóa thành công",
														Toast.LENGTH_LONG).show();
												showDsLop();

											//Hien thi thong bao khi xoa du lieu bi loi
											} else {
												Toast.makeText(ClassManagement.this,
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
		showDsLop();
	}

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
					//Dua danh sach lop vao ArrayAdapter_Lop
					adapter_lop = new ArrayAdapter_Lop(ClassManagement.this, R.layout.custom_listview_dslop, dsLop);

					//Gan ArrayAdapter_Lop vao listview
					lvLop.setAdapter(adapter_lop);
				//Khi truy van that bai
				} else {
					Toast.makeText(ClassManagement.this, e.getMessage(), Toast.LENGTH_LONG).show();
				}

			}

		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.class_management, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.QuanlySV) {
			Intent i = new Intent(ClassManagement.this, Home.class);
			startActivity(i);
			this.finish();
			return true;
		}
		if (id == R.id.Timkiem) {
			Intent i = new Intent(ClassManagement.this, SearchClass.class);
			startActivity(i);
			this.finish();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public void Themlop(View v) {
		//Tham chieu den dialog Them lop
		final Dialog lop = new Dialog(ClassManagement.this);
		lop.setContentView(R.layout.add_class);
		lop.setTitle("Thêm lớp");

		//Tham chieu den cac control tren dialog
		Button btnThemlop = (Button) lop.findViewById(R.id.bntThemlop);
		Button bntXoatrang = (Button) lop.findViewById(R.id.bntXoatrang);
		final EditText edtMalop = (EditText) lop.findViewById(R.id.edtMalop);
		final EditText edtTenlop = (EditText) lop.findViewById(R.id.edtTenlop);
		final EditText edtKhoahoc = (EditText) lop.findViewById(R.id.edtKhoahoc);

		//Gan gia tri tu dong cho Ma lop
		if(dsLop.size()>0){
			int vt=dsLop.size() - 1;
			String so = dsLop.get(vt).malop;
			int stt = Integer.parseInt(so.substring(1));		
			edtMalop.setText("L0" + (stt + 1));
		}
		else
			edtMalop.setText("L01");

		//Click nut them lop tren dialog
		btnThemlop.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Lay gia tri tren cac control tren dialog
				String malop = edtMalop.getText().toString();
				String tenlop = edtTenlop.getText().toString();
				String khoa_hoc = edtKhoahoc.getText().toString();
				tenlop = tenlop.trim();
				khoa_hoc = khoa_hoc.trim();

				//Kiem tra tinh hop le cua du lieu
				if(tenlop.equalsIgnoreCase("")){
					Toast.makeText(ClassManagement.this, "Vui lòng nhập Tên lớp", Toast.LENGTH_LONG).show();
				}
				else if(khoa_hoc.equalsIgnoreCase(""))
					Toast.makeText(ClassManagement.this, "Vui lòng nhập Khóa học", Toast.LENGTH_LONG).show();
				else {
					try{
						int khoahoc = Integer.parseInt(khoa_hoc);
						if(khoahoc<=0){
							Toast.makeText(ClassManagement.this, "Khóa học là số lớn hơn 0", Toast.LENGTH_LONG).show();
						}
						//Khi du lieu them vao hop le
						else{
							
							tenlop=tenlop.replaceAll("\\s+"," ");

							//Luu thong tin lop moi vao doi tuong ParseObject va luu tren Parse
							ParseObject dtLop = new ParseObject("Lop");
							dtLop.put("Malop", malop);
							dtLop.put("Tenlop", tenlop);
							dtLop.put("Khoahoc", khoahoc);
							dtLop.saveInBackground(new SaveCallback() {
								@Override
								public void done(com.parse.ParseException e) {
									//Khi viec them thanh cong
									if(e==null){
										Toast.makeText(ClassManagement.this, "Đã thêm thành công", Toast.LENGTH_LONG).show();
										showDsLop();
									}
									//Khi them du lieu that bai
									else{
										Toast.makeText(ClassManagement.this, e.getMessage(), Toast.LENGTH_LONG).show();
									}

								}
							});

							lop.dismiss();
						}
					}
					catch(Exception ex){
						Toast.makeText(ClassManagement.this, "Khóa học là số nguyên dương", Toast.LENGTH_LONG).show();
					}
				}
			}
		});
		//Click nut Xoa trang tren dialog Them lop
		bntXoatrang.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				//Xoa trang cac control tren dialog
				edtTenlop.setText("");
				edtKhoahoc.setText("");				
				edtTenlop.requestFocus();
				
			}
		});
		lop.show();
	}
}
