# Jawaban Week 4 Lab

## Latihan 1

### Eksperimen 1: @Controller vs @RestController
- /test/view → Menampilkan halaman HTML dengan pesan "Ini dari @Controller".
- /test/text → Menampilkan teks polos "Ini dari @Controller + @ResponseBody".
- Kesimpulan: @Controller tanpa @ResponseBody merujuk ke file HTML di templates, sedangkan dengan @ResponseBody mengirim data langsung ke browser.

### Eksperimen 2: Template Tidak Ada
- HTTP Status: 500
- Error: Internal Server Error

### Eksperimen 3: @RequestParam vs @PathVariable
| URL | Hasil |
|---|---|
| /greet | Selamat Pagi, Mahasiswa! |
| /greet?name=Budi | Selamat Pagi, Budi! |
| /greet/Ani/detail?lang=EN | Hello, Ani! |

### Pertanyaan Refleksi
1. Perbedaan: @Controller digunakan untuk aplikasi web tradisional di mana method akan mengembalikan nama template (seperti HTML via Thymeleaf), sedangkan @RestController (gabungan dari @Controller + @ResponseBody) digunakan untuk mengirim data langsung (seperti JSON atau teks polos) ke client.

    Penggunaan: Gunakan @Controller saat ingin membangun website lengkap dengan tampilan visual, dan gunakan @RestController saat ingin membuat API untuk dikonsumsi oleh aplikasi lain atau aplikasi mobile.
2. Keuntungan utamanya adalah efisiensi kode (DRY - Don't Repeat Yourself). Dengan satu template, kita memastikan tampilan (UI) tetap konsisten di berbagai fitur, serta mempermudah pemeliharaan karena perbaikan tampilan cukup dilakukan di satu file saja.
3. Alasannya: Agar terjadi pemisahan tugas (Separation of Concerns). Controller hanya bertugas mengatur lalu lintas permintaan web, sementara logika bisnis data diatur oleh Service.

    Jika langsung di manage Controller: Kode akan menjadi berantakan (tightly coupled), sulit untuk diuji (unit testing), dan logika data tersebut tidak bisa digunakan kembali oleh Controller lain jika dibutuhkan.
4. model.addAttribute: Menyimpan data ke dalam objek Model agar bisa dibaca oleh Thymeleaf untuk dirender menjadi halaman HTML.

    return products: Secara otomatis mengubah objek Java tersebut menjadi format data (seperti JSON) yang langsung dikirim ke browser tanpa melalui proses render HTML.
5. Hasil: Terjadi error (biasanya Status 400 - Bad Request atau 500).

    Kenapa: Karena terjadi ketidaksesuaian tipe data (Type Mismatch). Sistem mengharapkan parameter ID bertipe Long (angka), namun menerima String "abc" yang tidak bisa dikonversi menjadi angka oleh Spring.
6. Keuntungannya adalah organisasi kode yang lebih rapi. Kita tidak perlu mengulang penulisan alamat dasar di setiap method, dan jika suatu saat alamat utamanya ingin diubah (misal jadi /katalog), kita cukup mengubahnya di satu tempat saja di atas class.
7. Model sebagai Parameter: org.springframework.ui.Model adalah sebuah antarmuka yang digunakan Controller untuk mengirim data ke View (Thymeleaf).

    Model sebagai Class/Data: Merujuk pada class Product, yaitu representasi struktur data atau objek bisnis yang ada dalam aplikasi kita (disimpan dalam folder model).

## Latihan 2

### Eksperimen 1: Fragment Tidak Ada
- Apakah error?: Ya.

- Error message: Internal Server Error, status=500.

Kesimpulan: Jika nama fragment salah, Thymeleaf akan berhenti merender halaman dan melempar pengecualian (Internal Server Error 500) karena referensi kode yang diminta tidak ditemukan.

### Eksperimen 2: Static Resource
1. Menghapus th: dari href CSS:
- CSS masih bekerja? Ya
2.  Mencoba path yang salah:
-    Apakah halaman error? Tidak
-    Apakah CSS diterapkan? Tidak

- Kesimpulan: th:href="@{}" lebih baik karena memastikan path bersifat dinamis dan akan otomatis menyesuaikan dengan Context Path aplikasi, sehingga link tidak akan mati meskipun aplikasi di-deploy di lingkungan yang berbeda. Jika file CSS tidak ada, halaman HTML tetap akan dimuat secara normal namun tanpa gaya visual (unstyle), dan browser akan menerima status 404 Not Found untuk permintaan file tersebut.

### Pertanyaan Refleksi
1. Keuntungan Thymeleaf Fragment untuk Navbar & Footer:

Efisiensi (DRY): Menghindari penulisan kode berulang di setiap file HTML.

Konsistensi: Menjamin tampilan elemen tetap sama di semua halaman.

Mudah Diatur: Perubahan cukup dilakukan di satu file fragment (layout.html) dan otomatis berlaku di seluruh website.
2. Perbedaan static/ vs templates/ & Lokasi CSS:

static/: Menyimpan aset statis yang dikirim langsung ke browser tanpa diproses server (CSS, JS, Gambar).

templates/: Menyimpan file HTML dinamis yang diproses oleh mesin Thymeleaf di server sebelum dikirim.

CSS di static/: Karena isi file CSS bersifat tetap dan tidak perlu diubah secara dinamis oleh logika server.
3. th:replace vs th:insert:

th:replace: Menghapus tag penampung sepenuhnya dan menggantinya dengan tag dari fragment.

th:insert: Memasukkan isi fragment ke dalam tag penampung (tag penampung tetap ada sebagai pembungkus).
4. Alasan Menggunakan @{} untuk URL:

Context Path: Spring secara otomatis menyesuaikan alamat dasar aplikasi sehingga link tidak akan mati meskipun aplikasi dipindahkan ke server atau folder yang berbeda.

URL Encoding: Menangani karakter khusus secara otomatis agar URL tetap valid.
5. Akibat Jika Tidak Pakai DI (new ProductService()):

Tightly Coupled: Controller menjadi kaku dan sangat bergantung pada satu implementasi servis saja.

Sulit Diuji: Kita tidak bisa mengganti servis dengan data palsu (mocking) saat melakukan pengujian otomatis.

Manajemen Memori: Spring tidak bisa mengelola objek tersebut sebagai Singleton, sehingga bisa memboroskan memori jika tercipta banyak objek baru.