package fpoly.android.quanlysinhvien;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ArrayAdapter_Lop extends ArrayAdapter<Lop> {
	 
	 Activity context=null;
	 ArrayList<Lop>dsLop=null;
	 int layoutId;
	 
	 public ArrayAdapter_Lop(Activity context, int layoutId, ArrayList<Lop>arr){
			 super(context, layoutId, arr);
			 this.context=context;
			 this.layoutId=layoutId;
			 this.dsLop=arr;
	 }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
				
		LayoutInflater inflater=context.getLayoutInflater();
		convertView = inflater.inflate(layoutId, null);		
		
		
		// Dòng lệnh lấy TextView ra để hiển thị Mã lớp
		final TextView txtMalop = (TextView) convertView
				.findViewById(R.id.txtMalop);
		// lấy ra Lớp thứ position
		final Lop l = dsLop.get(position);
		// Đưa thông tin lên TextView
		txtMalop.setText(l.malop.toString());

		// Dòng lệnh lấy TextView ra để hiển thị Tên lớp
		final TextView txtTenlop = (TextView) convertView
				.findViewById(R.id.txtTenlop);
		// Đưa thông tin lên TextView
		txtTenlop.setText(l.tenlop.toString());

		// Dòng lệnh lấy TextView ra để hiển thị Khóa học
		final TextView txtKhoahoc = (TextView) convertView
				.findViewById(R.id.txtKhoahoc);
		// Đưa thông tin lên TextView
		txtKhoahoc.setText("" + l.khoahoc);
		
		
		return convertView;// trả về View này, tức là trả luôn
		// về các thông số mới mà ta vừa thay đổi
	}
	 
}

