# VolaToon! 📚🎨

## Deskripsi Aplikasi
**VolaToon!** adalah aplikasi membaca komik yang dikembangkan oleh **Team Volatile**, dirancang untuk memberikan pengalaman membaca komik yang menyenangkan dan interaktif. Aplikasi ini dilengkapi dengan berbagai fitur yang memudahkan pengguna dalam mencari, membaca, dan mengelola komik favorit mereka, baik secara online maupun offline.

## Fitur-Fitur Aplikasi
- **Pencarian Komik** 🔍: Temukan komik favorit Anda dengan mudah.
- **Membaca Komik (Chapter)** 📖: Nikmati pengalaman membaca yang mulus.
- **Bookmark** ⭐: Tandai komik favorit Anda untuk akses cepat.
- **History Baca** 📜: Lacak riwayat baca Anda.
- **Login / Register** 🔑: Buat akun atau masuk untuk pengalaman yang dipersonalisasi.
- **Notifikasi** 🔔: Dapatkan update komik terbaru.
- **Komunitas** 💬: Berinteraksi dengan pembaca lain melalui komentar.
- **Download Komik Offline** 📥: Simpan komik untuk dibaca tanpa internet.
- **Komentar** 🗨️: Tinggalkan komentar pada setiap chapter.
- **Filter Genre / Status** 🎭: Temukan komik berdasarkan genre atau status (ongoing/completed).
- **Rating Komik** ⭐⭐⭐⭐: Berikan penilaian pada komik yang Anda baca.
- **Dark Mode** 🌙: Mode gelap untuk kenyamanan membaca di malam hari.

## Tech Stack
- **Bahasa Pemrograman**: Kotlin
- **IDE**: Android Studio
- **Database**: Prisma (PostgreSQL)
- **Backend**: Node.js
- **Library**:
  - **Retrofit**: Untuk koneksi API
  - **Glide**: Untuk pemuatan gambar
  - **Room**: Database lokal
  - **RecyclerView**: Untuk menampilkan daftar komik
  - **Coroutines**: Untuk operasi asynchronous
  - **Jetpack Compose**: UI modern dan responsif

## Pembagian Tugas Tim
- **Rafael**: Backend dan Logika
- **Rafhael**: Frontend (UI/UX)
- **Prima**: Backend dan Logika
- **Kelvin**: Backend dan Logika
- **Jason**: Frontend (UI/UX)
- **Alvaro**: Frontend (UI/UX)

## Struktur Aplikasi

### Home (Not Logged In)
- Splash Screen
- Banner Komik
- List Komik Populer
- Login / Register
- Filter Genre / Status
- Search Bar
- Settings

### Home (Logged In)
- Banner Komik
- List Komik Favorit
- Profil Pengguna
- Filter Genre / Status
- Search Bar
- Settings

### Detail Komik
- Cover Komik
- Sinopsis / Detail Komik
- Rating Komik
- Penulis (Author)
- Status (Ongoing / Completed)
- Jumlah Chapter
- List Chapter
- Tombol Readlist

### Membaca Komik
- Judul Komik dan Chapter
- Isi Komik (Swipe Mode)
- Bookmark Chapter
- Like & Komentar
- Tanggal Rilis
- Download Chapter

### History, Readlist, Bookmark
- List Komik yang Dibaca
- Unbookmark & Search Bar

### Profil Pengguna
- Username
- Foto Profil
- Status Pembaca
- Logout

## Cara Memulai 🚀

### Prasyarat
- **Android Studio** versi terbaru
- **Node.js** dan **npm** (untuk backend)
- **Prisma CLI** untuk manajemen database

### Setup Project

#### Clone Repository
```bash
git clone https://github.com/volatile-team/volatoon.git
cd volatoon
```


## Kontribusi 💡
Kami menerima kontribusi dari siapa saja! Jika Anda tertarik untuk berkontribusi:

### Fork repository ini.
### Buat branch baru (git checkout -b fitur-baru).
### Lakukan perubahan dan commit (git commit -m 'Tambah fitur baru').
### Push ke branch (git push origin fitur-baru).
### Buat pull request.

## Lisensi 📄
Proyek ini dilisensikan di bawah MIT License.

Kontak
Jika ada pertanyaan atau masalah, Anda dapat menghubungi salah satu anggota tim:

Email: team@volatile.xyz
Discord: Somebodytoloves
