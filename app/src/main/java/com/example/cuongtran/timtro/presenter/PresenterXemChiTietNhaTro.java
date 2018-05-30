package com.example.cuongtran.timtro.presenter;

import com.example.cuongtran.timtro.entity.Chat;
import com.example.cuongtran.timtro.entity.TinDang;
import com.example.cuongtran.timtro.model.data.ModelXemChiTietNhaTro;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PresenterXemChiTietNhaTro implements ModelXemChiTietNhaTro.ICallBack
{
    ModelXemChiTietNhaTro modelXemChiTietNhaTro;
    IchiTietNhaTro ichiTietNhaTro ;

    public PresenterXemChiTietNhaTro (IchiTietNhaTro ichiTietNhaTro){
        this.ichiTietNhaTro= ichiTietNhaTro;
        modelXemChiTietNhaTro= new ModelXemChiTietNhaTro(this);

    }
    public void luuTin(String idNha,String idTK){
        modelXemChiTietNhaTro.luuTin(idNha,idTK);
    }
    public void getData(String idnha){
        modelXemChiTietNhaTro.getData(idnha);
    }
    public void addChat(String text,String tentk,String idNhanDuoc){
        if(text.equals("")){
            ichiTietNhaTro.showMess("Không được bỏ trống !");
            //Toast.makeText(context, "Không được bỏ trống !", Toast.LENGTH_SHORT).show();

        }else {
            Date currentTime = Calendar.getInstance().getTime();
            Timestamp timestamp = new Timestamp(currentTime);
            Chat chat= new Chat(tentk,text,timestamp);
            ichiTietNhaTro.addChatOffline(chat);
            //add to Firebase
            modelXemChiTietNhaTro.addChatToFireBase(tentk,text,idNhanDuoc);
        }
    }


    @Override
    public void returnData(TinDang tinDang, String urlAvatar, String nameUser, ArrayList<Chat> chats) {
        ichiTietNhaTro.SetData(tinDang,urlAvatar,nameUser,chats);
    }

    @Override
    public void showMess(String mess) {
        ichiTietNhaTro.showMess(mess);
    }

    @Override
    public void dissmissDialog() {
        ichiTietNhaTro.dissmissDialog();
    }


    public interface  IchiTietNhaTro{
        void addChatOffline(Chat chat);
        void SetData(TinDang tinDang,String urlAvatar,String nameUser,ArrayList<Chat> chats);
        void showMess(String mess);
        void dissmissDialog();
    }
}
