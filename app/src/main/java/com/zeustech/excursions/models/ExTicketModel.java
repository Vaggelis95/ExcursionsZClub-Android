package com.zeustech.excursions.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.zeustech.excursions.customViews.DateManager;

import java.util.IllegalFormatException;
import java.util.Locale;

import androidx.annotation.NonNull;

public class ExTicketModel implements Parcelable {

    private int year, intno;

    private String picPath;

    private String excDescr, excDate, ticket, hotel, roomCode;

    private String pickUpPoint, pickUpTime, name;

    private Float price;

    private int adults, children, status;

    private String companyDescr, address, zip, gnto, vrn, tel, doy, extraInfo, transDate;

    public String getFormattedTransDate() {
        return transDate != null ?
                DateManager.convertDate(transDate, "yyyyMMdd", "dd/MM/yyyy") : null;
    }

    public String getFormattedExcDate() {
        return excDate != null ?
                DateManager.convertDate(excDate, "yyyyMMdd", "dd/MM/yyyy") : null;
    }

    public int getYear() {
        return year;
    }

    public int getIntno() {
        return intno;
    }

    public String getPicPath() {
        return picPath;
    }

    public String getExcDescr() {
        return excDescr;
    }

    public String getExcDate() {
        return excDate;
    }

    public String getTicket() {
        return ticket;
    }

    public String getHotel() {
        return hotel;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public String getPickUpPoint() {
        return pickUpPoint;
    }

    public String getPickUpTime() {
        return pickUpTime;
    }

    public String getName() {
        return name;
    }

    public Float getPrice() {
        return price;
    }

    public int getAdults() {
        return adults;
    }

    public int getChildren() {
        return children;
    }

    public int getStatus() {
        return status;
    }

    public String getCompanyDescr() {
        return companyDescr;
    }

    public String getAddress() {
        return address;
    }

    public String getZip() {
        return zip;
    }

    public String getGnto() {
        return gnto;
    }

    public String getVrn() {
        return vrn;
    }

    public String getTel() {
        return tel;
    }

    public String getDoy() {
        return doy;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public String getTransDate() {
        return transDate;
    }

    public String getFormattedPrice() {
        try {
            return String.format(Locale.US, "%.2f", price);
        } catch (IllegalFormatException e) {
            e.printStackTrace();
        }
        return String.valueOf(price);
    }

    @NonNull
    @Override
    public String toString() {
        return "ExTicketModel{" +
                "year=" + year +
                ", intno=" + intno +
                ", picPath='" + picPath + '\'' +
                ", excDescr='" + excDescr + '\'' +
                ", excDate='" + excDate + '\'' +
                ", ticket='" + ticket + '\'' +
                ", hotel='" + hotel + '\'' +
                ", roomCode='" + roomCode + '\'' +
                ", pickUpPoint='" + pickUpPoint + '\'' +
                ", pickUpTime='" + pickUpTime + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", adults=" + adults +
                ", children=" + children +
                ", status=" + status +
                ", companyDescr='" + companyDescr + '\'' +
                ", address='" + address + '\'' +
                ", zip='" + zip + '\'' +
                ", gnto='" + gnto + '\'' +
                ", vrn='" + vrn + '\'' +
                ", tel='" + tel + '\'' +
                ", doy='" + doy + '\'' +
                ", extraInfo='" + extraInfo + '\'' +
                ", transDate='" + transDate + '\'' +
                '}';
    }

    protected ExTicketModel(Parcel in) {
        year = in.readInt();
        intno = in.readInt();
        picPath = in.readString();
        excDescr = in.readString();
        excDate = in.readString();
        ticket = in.readString();
        hotel = in.readString();
        roomCode = in.readString();
        pickUpPoint = in.readString();
        pickUpTime = in.readString();
        name = in.readString();
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readFloat();
        }
        adults = in.readInt();
        children = in.readInt();
        status = in.readInt();
        companyDescr = in.readString();
        address = in.readString();
        zip = in.readString();
        gnto = in.readString();
        vrn = in.readString();
        tel = in.readString();
        doy = in.readString();
        extraInfo = in.readString();
        transDate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(year);
        dest.writeInt(intno);
        dest.writeString(picPath);
        dest.writeString(excDescr);
        dest.writeString(excDate);
        dest.writeString(ticket);
        dest.writeString(hotel);
        dest.writeString(roomCode);
        dest.writeString(pickUpPoint);
        dest.writeString(pickUpTime);
        dest.writeString(name);
        if (price == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(price);
        }
        dest.writeInt(adults);
        dest.writeInt(children);
        dest.writeInt(status);
        dest.writeString(companyDescr);
        dest.writeString(address);
        dest.writeString(zip);
        dest.writeString(gnto);
        dest.writeString(vrn);
        dest.writeString(tel);
        dest.writeString(doy);
        dest.writeString(extraInfo);
        dest.writeString(transDate);
    }

    public static final Creator<ExTicketModel> CREATOR = new Creator<ExTicketModel>() {
        @Override
        public ExTicketModel createFromParcel(Parcel in) {
            return new ExTicketModel(in);
        }

        @Override
        public ExTicketModel[] newArray(int size) {
            return new ExTicketModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @NonNull
    public String getReceipt() {
        return  "Ticket:           " + ticket +
                "\n--------------------------------" +
                "\nCustomer:         " + name +
                "\nAdults:           " + adults +
                "\nChildren:         " + children +
                "\nDate:             " + getFormattedExcDate() +
                "\nHotel:            " + hotel +
                "\nRoom No:          " + roomCode +
                "\nPickup point:     " + pickUpPoint +
                "\nPickup time:      " + pickUpTime +
                "\nPrice:            " + price + " €" +
                "\nTransaction Date: " + getFormattedTransDate() +
                "\n\n--------------------------------\n\n" +
                companyDescr + "\n" + address + " " + zip +
                "\nVRN:  " + vrn +
                "\nDOY:  " + doy +
                "\nGNTO: " + gnto +
                "\nTEL:  " + tel +
                "\n\nCopy of the original ticket:" +
                "\n\n";
    }

}
