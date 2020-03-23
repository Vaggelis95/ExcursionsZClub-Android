package com.zeustech.excursions.bixolon.manager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.bxl.BXLConst;
import com.bxl.config.editor.BXLConfigLoader;
import com.zeustech.excursions.bixolon.PrinterControl.BixolonPrinter;
import com.zeustech.excursions.callbacks.CompletionHandler;
import com.zeustech.excursions.models.ExTicketModel;

import java.util.Objects;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import jpos.POSPrinterConst;

public class BixolonManager {

    private final static String DEFAULT_PRINTER_NAME = "SPP-R210";
    private final static String FAILURE_MSG = "Connection failed";
    private final static String SUCCESS_MSG = "Connection established";

    private static volatile BixolonManager INSTANCE = null;
    private final BixolonPrinter bxlPrinter;

    public static synchronized BixolonManager getInstance(@NonNull Context context) {
        if(INSTANCE == null) {
            try {
                INSTANCE = new BixolonManager(context);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return INSTANCE;
    }

    private BixolonManager(@NonNull Context context) {
        bxlPrinter = new BixolonPrinter(context.getApplicationContext());
    }

    private void open(@NonNull final String name, @NonNull final String address, @NonNull final CompletionHandler<String> completionHandler) {
        new Thread(() -> {
            final boolean success = bxlPrinter.printerOpen(
                    BXLConfigLoader.DEVICE_BUS_BLUETOOTH,
                    name,
                    address,
                    true);
            final String message = success ? SUCCESS_MSG : FAILURE_MSG;
            new Handler(Looper.getMainLooper()).post(() -> {
                if (success) {
                    completionHandler.onSuccess(message, -1);
                } else {
                    completionHandler.onFailure(message, -1);
                }
            });
        }).start();
    }

    private void connectToDefaultPrinter(@NonNull CompletionHandler<String> completion) {
        String address = getDefaultPrinterAddress();
        if (address != null) {
            open(DEFAULT_PRINTER_NAME, address, completion);
        } else {
            completion.onFailure(FAILURE_MSG, -1);
        }
    }

    private void close() {
        bxlPrinter.printerClose();
    }

    private void print(@NonNull PrintType type, @NonNull String text) {
        int alignment = BixolonPrinter.ALIGNMENT_LEFT; // alignment
        int attribute = BixolonPrinter.ATTRIBUTE_NORMAL; // attributes (+ attribute |= "other attribute")
        int textSize = 1;
        bxlPrinter.setCharacterSet(BXLConst.CS_1253_GREEK);
        switch (type) {
            case TEXT: {
                bxlPrinter.printText(text, alignment, attribute, textSize);
                break;
            }
            case BARCODE: {
                bxlPrinter.printBarcode(
                        text,
                        POSPrinterConst.PTR_BCS_QRCODE,
                        8, 8,
                        POSPrinterConst.PTR_BC_CENTER,
                        POSPrinterConst.PTR_BC_TEXT_BELOW);
                break;
            }
            default: break;
        }
    }

    private void printTicket(@NonNull ExTicketModel ticket) {
        String receipt = ticket.getReceipt();
        print(PrintType.TEXT, receipt);
        print(PrintType.BARCODE, ticket.getTicket());
        print(PrintType.TEXT, "\n\n\n");
    }

    public void connectPrintDisconnect(@NonNull final ExTicketModel receipt, @NonNull final CompletionHandler<String> completion) {
        connectToDefaultPrinter(new CompletionHandler<String>() { // connect
            @Override
            public void onSuccess(@NonNull String model, int status) {
                printTicket(receipt); // If connected print
                new Handler().postDelayed(() -> {
                    close(); // disconnect after 4 seconds
                    completion.onSuccess("Success!", status);
                }, 3000);
            }

            @Override
            public void onFailure(@Nullable String description, int status) {
                completion.onFailure(description, status);
            }
        });
    }

    @Nullable
    private String getDefaultPrinterAddress() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> bondedDeviceSet = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : bondedDeviceSet) {
            if (Objects.equals(device.getName(), DEFAULT_PRINTER_NAME)) {
                return device.getAddress();
            }
        }
        return null;
    }

}


