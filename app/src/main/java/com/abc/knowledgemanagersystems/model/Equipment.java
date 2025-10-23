package com.abc.knowledgemanagersystems.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey; // ĐÃ XÓA FOREIGN KEY TỚI INVENTORY
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
// Bỏ hoàn toàn phần foreignKeys liên quan đến InventoryItem
@Entity(tableName = "equipment")
public class Equipment {

    @PrimaryKey
    @NotNull
    @ColumnInfo(name = "equipment_id")
    private int id;

    private String name; // Tên máy, ví dụ: "Máy ly tâm"
    private String model; // Ví dụ: "Eppendorf 5424 R"

    @ColumnInfo(name = "serial_number") // Giữ lại trường này của bạn
    private String serialNumber;

    // (TRƯỜNG MỚI cho Yêu cầu #10)
    // Đường dẫn để tải file PDF hướng dẫn sử dụng
    @ColumnInfo(name = "manual_pdf_url")
    private String manualPdfUrl;

    // (TRƯỜNG MỚI cho Yêu cầu #10)
    // Trạng thái: "Available", "Under Maintenance"
    private String status;

    /* * ĐÃ XÓA CÁC TRƯỜNG SAI:
     * - private double quantity;
     * - private String formula;
     * - private int inventoryItemId;
     * - private int userId; (Việc một cái máy "thuộc về" ai không quan trọng
     * bằng việc "ai đặt lịch" nó, việc này sẽ lưu ở bảng Booking)
     */
}