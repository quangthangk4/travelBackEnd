package com.travel.travelling.constant;

import lombok.Getter;

@Getter
public enum VietnamAirport {
    TAN_SON_NHAT("Sân bay Quốc tế Tân Sơn Nhất", 10.8188, 106.652, "Việt Nam", "VN", "SGN", "Thành phố Hồ Chí Minh"),
    NOI_BAI("Sân bay Quốc tế Nội Bài", 21.221201, 105.806999, "Việt Nam", "VN", "HAN", "Hà Nội"),
    DA_NANG("Sân bay Quốc tế Đà Nẵng", 16.0439, 108.198997, "Việt Nam", "VN", "DAD", "Đà Nẵng"),
    PHU_QUOC("Sân bay Quốc tế Phú Quốc", 10.1698, 103.9931, "Việt Nam", "VN", "PQC", "Kiên Giang"),
    CAM_RANH("Sân bay Quốc tế Cam Ranh", 11.9982, 109.219002, "Việt Nam", "VN", "CXR", "Khánh Hòa"),
    PHU_BAI("Sân bay Quốc tế Phú Bài", 16.400628, 107.703094, "Việt Nam", "VN", "HUI", "Thừa Thiên Huế"),
    LIEN_KHUONG("Sân bay Liên Khương", 11.750556, 108.366997, "Việt Nam", "VN", "DLI", "Lâm Đồng"),
    CON_DAO("Sân bay Côn Đảo", 8.73183, 106.633003, "Việt Nam", "VN", "VCS", "Bà Rịa - Vũng Tàu"),
    PHU_CAT("Sân bay Phù Cát", 13.955, 109.042, "Việt Nam", "VN", "UIH", "Bình Định"),
    CAT_BI("Sân bay Quốc tế Cát Bi", 20.81686, 106.722994, "Việt Nam", "VN", "HPH", "Hải Phòng"),
    BUON_MA_THUOT("Sân bay Buôn Ma Thuột", 12.668299675, 108.120002747, "Việt Nam", "VN", "BMV", "Đắk Lắk"),
    CAN_THO("Sân bay Quốc tế Cần Thơ", 10.0851, 105.711998, "Việt Nam", "VN", "VCA", "Cần Thơ"),
    PLEIKU("Sân bay Pleiku", 14.0045, 108.017, "Việt Nam", "VN", "PXU", "Gia Lai"),
    VINH("Sân bay Vinh", 18.7376, 105.671, "Việt Nam", "VN", "VII", "Nghệ An"),
    CHU_LAI("Sân bay Chu Lai", 15.4033, 108.706001, "Việt Nam", "VN", "VCL", "Quảng Nam"),
    DONG_HOI("Sân bay Đồng Hới", 17.515, 106.590556, "Việt Nam", "VN", "VDH", "Quảng Bình"),
    RACH_GIA("Sân bay Rạch Giá", 9.95803, 105.13238, "Việt Nam", "VN", "VKG", "Kiên Giang"),
    THO_XUAN("Sân bay Thọ Xuân", 19.901667, 105.467778, "Việt Nam", "VN", "THD", "Thanh Hóa"),
    DIEN_BIEN("Sân bay Điện Biên Phủ", 21.397499, 103.008003, "Việt Nam", "VN", "DIN", "Điện Biên"),
    DONG_TAC("Sân bay Đông Tác", 13.0496, 109.334, "Việt Nam", "VN", "TBB", "Phú Yên"),
    CA_MAU("Sân bay Cà Mau", 9.177667, 105.177778, "Việt Nam", "VN", "CAH", "Cà Mau"),
    VAN_DON("Sân bay Quốc tế Vân Đồn", 21.120693, 107.41539, "Việt Nam", "VN", "VDO", "Quảng Ninh");

    private final String tenSanBay;
    private final double viDo;
    private final double kinhDo;
    private final String quocGia;
    private final String maQuocGia;
    private final String maIATA;
    private final String tinhThanh;

    VietnamAirport(String tenSanBay, double viDo, double kinhDo, String quocGia, String maQuocGia, String maIATA, String tinhThanh) {
        this.tenSanBay = tenSanBay;
        this.viDo = viDo;
        this.kinhDo = kinhDo;
        this.quocGia = quocGia;
        this.maQuocGia = maQuocGia;
        this.maIATA = maIATA;
        this.tinhThanh = tinhThanh;
    }

    public static boolean isValidIataCode(String iataCode) {
        for (VietnamAirport airport : values()) {
            if (airport.getMaIATA().equalsIgnoreCase(iataCode)) {
                return true;
            }
        }
        return false;
    }
}

