package com.example.music.data.local

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.music.FAVOURITES_KEY
import com.example.music.LIST_FOLDER_SOUND_FAVORITE
import com.example.music.LIST_SOUND_FAVORITE
import com.example.music.SHARED_PREFERENCES_FILE_NAME
import com.example.music.data.Resource
import com.example.music.data.dto.localprank.*
import com.example.music.data.dto.login.LoginRequest
import com.example.music.data.dto.login.LoginResponse
import com.example.music.data.dto.prank.SoundFolderPrank
import com.example.music.data.dto.prank.SoundPrank
import com.example.music.data.error.PASS_WORD_ERROR
import com.example.music.utils.ExtensionConstants
import com.ntduc.datetimeutils.getDateTimeFromMillis
import com.ntduc.datetimeutils.isToday
import com.ntduc.datetimeutils.isYesterday
import com.ntduc.fileutils.getAudios
import com.ntduc.fileutils.getImages
import com.ntduc.fileutils.getVideos
import com.orhanobut.hawk.Hawk
import java.io.File
import java.util.*
import javax.inject.Inject

/**
 * Created by TruyenIT
 */

class LocalData @Inject constructor(val context: Context) {

    /**
     * phương thức này kiểm tra đăng nhập bằng cách so sánh yêu cầu đăng nhập được truyền vào với một giá trị mặc định đã được cung cấp.
     * Nếu đăng nhập thành công, nó trả về một đối tượng LoginResponse. Nếu không, nó trả về một đối tượng DataError.
     *
     * */
    fun doLogin(loginRequest: LoginRequest): Resource<LoginResponse> {
        if (loginRequest == LoginRequest("ahmed@ahmed.ahmed", "ahmed")) {
            return Resource.Success(
                LoginResponse(
                    "123", "Ahmed", "Mahmoud",
                    "FrunkfurterAlle", "77", "12000", "Berlin",
                    "Germany", "ahmed@ahmed.ahmed"
                )
            )
        }
        return Resource.DataError(PASS_WORD_ERROR)
    }

    /**
     * getCachedFavourites(): phương thức này lấy danh sách các yêu thích từ bộ nhớ cache và trả về một đối tượng Set<String>
     * */
    fun getCachedFavourites(): Resource<Set<String>> {
        val sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, 0)
        return Resource.Success(sharedPref.getStringSet(FAVOURITES_KEY, setOf()) ?: setOf())
    }

    /**
     * isFavourite(id: String): phương thức này kiểm tra xem một id đã được đánh dấu yêu thích hay chưa.
     * Nó lấy danh sách yêu thích từ bộ nhớ cache và kiểm tra xem id được truyền vào có nằm trong danh sách hay không.
     * Nếu có, nó trả về true, ngược lại, nó trả về false.
     * */
    fun isFavourite(id: String): Resource<Boolean> {
        val sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, 0)
        val cache = sharedPref.getStringSet(FAVOURITES_KEY, setOf<String>()) ?: setOf()
        return Resource.Success(cache.contains(id))
    }

    /**
     * cacheFavourites(ids: Set<String>): phương thức này lưu danh sách yêu thích vào bộ nhớ cache và trả về true nếu lưu thành công.
     * */
    fun cacheFavourites(ids: Set<String>): Resource<Boolean> {
        val sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, 0)
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putStringSet(FAVOURITES_KEY, ids)
        editor.apply()
        val isSuccess = editor.commit()
        return Resource.Success(isSuccess)
    }

    /**
     *removeFromFavourites(id: String): phương thức này xóa id khỏi danh sách yêu thích và trả về true nếu xóa thành công.
     */

    fun removeFromFavourites(id: String): Resource<Boolean> {
        val sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, 0)
        var set = sharedPref.getStringSet(FAVOURITES_KEY, mutableSetOf<String>())?.toMutableSet() ?: mutableSetOf()
        if (set.contains(id)) {
            set.remove(id)
        }
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.clear()
        editor.apply()
        editor.commit()
        editor.putStringSet(FAVOURITES_KEY, set)
        editor.apply()
        val isSuccess = editor.commit()
        return Resource.Success(isSuccess)
    }

    /**
     *favoriteSound(item: SoundPrank): phương thức này thực hiện thao tác yêu thích cho một âm thanh cụ thể.
     * Nó kiểm tra xem âm thanh đã được đánh dấu yêu thích trước đó hay chưa và trả về true
     * nếu âm thanh được đánh dấu yêu thích sau khi phương thức được gọi và ngược lại.
     */

    fun favoriteSound(item: SoundPrank): Boolean {
        var listSound = Hawk.get<ArrayList<SoundPrank>>(LIST_SOUND_FAVORITE, ArrayList())
        val itr = listSound.listIterator()

        while (itr.hasNext()) {

            if (listSound.size == 0) {
                listSound.add(item)
                Hawk.put<ArrayList<SoundPrank>>(LIST_SOUND_FAVORITE, listSound)
                return true
            } else {
                var index = itr.nextIndex()
                if (itr.next().id == item.id) {
                    listSound.removeAt(index)
                    Hawk.put<ArrayList<SoundPrank>>(LIST_SOUND_FAVORITE, listSound)
                    return false
                }
            }
        }
        listSound.add(item)
        Hawk.put<ArrayList<SoundPrank>>(LIST_SOUND_FAVORITE, listSound)
        return true


    }

    /**
     * favoriteSoundFolder(item: SoundPrank): phương thức này thực hiện thao tác yêu thích cho một thư mục âm thanh cụ thể.
     * Nó tìm kiếm trong danh sách các thư mục âm thanh đã được đánh dấu yêu thích trước đó. Nếu thư mục âm thanh đang được thao tác
     * đã được đánh dấu yêu thích trước đó, phương thức sẽ xóa nó khỏi danh sách yêu thích và trả về false.
     * Nếu thư mục âm thanh chưa được đánh dấu yêu thích trước
     * */
    fun favoriteSoundFolder(item: SoundPrank): Boolean {
        Log.e("TAG", "favoriteSoundFolder: favorite" )
        var listSoundFolder = Hawk.get<ArrayList<SoundFolderPrank>>(LIST_FOLDER_SOUND_FAVORITE, ArrayList())
        val itr = listSoundFolder.listIterator()

        while (itr.hasNext()) {

            if (listSoundFolder.size == 0) {
                var arraySound= arrayListOf<SoundPrank>(item)
                var folderSound = SoundFolderPrank(item.group.id,item.group.createdAt, item.group.name, item.group.image, null,arraySound)
                listSoundFolder.add(folderSound)
                Hawk.put<ArrayList<SoundFolderPrank>>(LIST_FOLDER_SOUND_FAVORITE, listSoundFolder)
                return true
            } else {
                var index = itr.nextIndex()
                if (itr.next().id == item.group.id) {
                    if (listSoundFolder[index].arraySound.contains(item)){
                        listSoundFolder[index].arraySound.remove(item)
                        if (listSoundFolder[index].arraySound.size==0){
                            listSoundFolder.removeAt(index)
                        }
                        Hawk.put<ArrayList<SoundFolderPrank>>(LIST_FOLDER_SOUND_FAVORITE, listSoundFolder)
                        return false
                    }else{
                        listSoundFolder[index].arraySound.add(item)
                        Hawk.put<ArrayList<SoundFolderPrank>>(LIST_FOLDER_SOUND_FAVORITE, listSoundFolder)
                        return true
                    }

                }
            }
        }
        var arraySound= arrayListOf<SoundPrank>(item)
        var folderSound = SoundFolderPrank(item.group.id,item.group.createdAt, item.group.name, item.group.image, null,arraySound)
        listSoundFolder.add(folderSound)
        Hawk.put<ArrayList<SoundFolderPrank>>(LIST_FOLDER_SOUND_FAVORITE, listSoundFolder)
        return true


    }

    fun checkFavoriteSoundFolder(item: SoundPrank): Boolean {
        var listSoundFolder = Hawk.get<ArrayList<SoundFolderPrank>>(LIST_FOLDER_SOUND_FAVORITE, ArrayList())
        val itr = listSoundFolder.listIterator()

        while (itr.hasNext()) {

            if (listSoundFolder.size == 0) {
                return false
            } else {
                var index = itr.nextIndex()
                if (itr.next().arraySound.contains(item)) {
                    return true
                }
            }
        }
        return false
    }

    fun checkFavoriteFolderSound(item: SoundFolderPrank): Boolean {
        var listSoundFolder = Hawk.get<ArrayList<SoundFolderPrank>>(LIST_FOLDER_SOUND_FAVORITE, ArrayList())
        val itr = listSoundFolder.listIterator()

        while (itr.hasNext()) {

            if (listSoundFolder.size == 0) {
                return false
            } else {
                var index = itr.nextIndex()
                if (itr.next().id==item.id) {
                    return true
                }
            }
        }
        return false
    }
    fun checkFavoriteSound(item: SoundPrank): Boolean {
        var listSound = Hawk.get<ArrayList<SoundPrank>>(LIST_SOUND_FAVORITE, ArrayList())
        val itr = listSound.listIterator()

        while (itr.hasNext()) {

            if (listSound.size == 0) {
                return false
            } else {
                var index = itr.nextIndex()
                if (itr.next().id == item.id) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * image
     */
    fun getAllImage(): Resource<List<MyFolderImage>> {
        val images = context.getImages(types = ExtensionConstants.IMAGE.toList())
        val temp = images.sortedWith { o1, o2 ->
            o2.dateModified!!.compareTo(o1.dateModified!!)
        }
        val result = ArrayList<MyImage>()
        temp.forEach {
            val myFile = MyFile(
                it.title,
                it.displayName,
                it.mimeType,
                it.size,
                it.dateAdded,
                it.dateModified,
                it.data
            )
            val myImage = MyImage(myFile, it.height, it.width)
            result.add(myImage)
        }
        val listRecent = filterRecentPhoto(result)
        return Resource.Success(listRecent)
    }


    private fun filterRecentPhoto(photos: List<MyImage>): ArrayList<MyFolderImage> {
        val listFolderPhoto = ArrayList<MyFolderImage>()
        for (photo in photos) {
            val pos = checkFolderPhotoByTime(photo, listFolderPhoto)
            if (pos >= 0) {
                listFolderPhoto[pos].list.add(photo)
            } else {
                val file = File(photo.myFile!!.data!!)
                val folder = MyFile(
                    getNameFolderByTime(file.lastModified()),
                    getNameFolderByTime(file.lastModified()),
                    "",
                    file.length(),
                    file.lastModified(),
                    file.lastModified(),
                    file.parentFile?.path ?: ""
                )
                val folderPhoto = MyFolderImage(folder)
                folderPhoto.list.add(photo)

                listFolderPhoto.add(folderPhoto)
            }
        }
        return listFolderPhoto
    }

    private fun checkFolderPhotoByTime(
        photo: MyImage,
        listFolderPhoto: ArrayList<MyFolderImage>
    ): Int {
        for (i in listFolderPhoto.indices) {
            if (getNameFolderByTime(photo.myFile!!.dateModified) == listFolderPhoto[i].folder.title) {
                return i
            }
        }
        return -1
    }

    private fun getNameFolderByTime(time: Long?): String {
        if (time == null) return "Unknown"

        val date = Date(time)
        return if (date.isToday()) {
            "Today"
        } else if (date.isYesterday()) {
            "Yesterday"
        } else {
            getDateTimeFromMillis(time, "MMM dd", Locale.ENGLISH)
        }
    }

    /**
     * audio
     */


    fun getAllAudio(): Resource<List<MyFolderAudio>> {
        val audios = context.getAudios(types = ExtensionConstants.MUSIC.toList(), isMusic = true)
        val temp = audios.sortedWith { o1, o2 ->
            o2.dateModified!!.compareTo(o1.dateModified!!)
        }
        val result = ArrayList<MyAudio>()
        temp.forEach {

            val myFile = MyFile(
                it.title,
                it.displayName,
                it.mimeType,
                it.size,
                it.dateAdded,
                it.dateModified,
                it.data
            )
            val myAudio = MyAudio(myFile, it.album, it.artist, it.duration)
            result.add(myAudio)
        }
        val listRecent = filterRecentAudio(result)
        return Resource.Success(listRecent)
    }


    private fun filterRecentAudio(audios: List<MyAudio>): ArrayList<MyFolderAudio> {
        val listFolderAudio = ArrayList<MyFolderAudio>()
        for (audio in audios) {
            val pos = checkFolderAudioByTime(audio, listFolderAudio)
            if (pos >= 0) {
                listFolderAudio[pos].list.add(audio)
            } else {
                val file = File(audio.myFile!!.data!!)
                val folder = MyFile(
                    getNameFolderByTime(file.lastModified()),
                    getNameFolderByTime(file.lastModified()),
                    "",
                    file.length(),
                    file.lastModified(),
                    file.lastModified(),
                    file.parentFile?.path ?: ""
                )
                val folderAudio = MyFolderAudio(folder)
                folderAudio.list.add(audio)

                listFolderAudio.add(folderAudio)
            }
        }
        return listFolderAudio
    }

    private fun checkFolderAudioByTime(
        audio: MyAudio,
        listFolderAudio: ArrayList<MyFolderAudio>
    ): Int {
        for (i in listFolderAudio.indices) {
            if (getNameFolderByTime(audio.myFile!!.dateModified) == listFolderAudio[i].folder.title) {
                return i
            }
        }
        return -1
    }


    /**
     * video
     */
    fun getAllVideo(): Resource<List<MyFolderVideo>> {
        val videos = context.getVideos(types = ExtensionConstants.VIDEO.toList())
        val temp = videos.sortedWith { o1, o2 ->
            o2.dateModified!!.compareTo(o1.dateModified!!)
        }
        val result = ArrayList<MyVideo>()
        temp.forEach {
            val myFile = MyFile(
                it.title,
                it.displayName,
                it.mimeType,
                it.size,
                it.dateAdded,
                it.dateModified,
                it.data
            )
            val myVideo = MyVideo(myFile, it.height, it.width, it.album, it.artist, it.duration, it.bucketID, it.bucketDisplayName, it.resolution)
            result.add(myVideo)
        }

        val listRecent = filterRecentVideo(result)
        return Resource.Success(listRecent)
    }

    fun getAllVideoFromFolder(path:String): Resource<List<MyVideo>> {
        val videos = context.getVideos(directoryPath = path, types = ExtensionConstants.VIDEO.toList())
        val temp = videos.sortedWith { o1, o2 ->
            o2.dateModified!!.compareTo(o1.dateModified!!)
        }
        val result = ArrayList<MyVideo>()
        temp.forEach {
            val myFile = MyFile(
                it.title,
                it.displayName,
                it.mimeType,
                it.size,
                it.dateAdded,
                it.dateModified,
                it.data
            )
            val myVideo = MyVideo(myFile, it.height, it.width, it.album, it.artist, it.duration, it.bucketID, it.bucketDisplayName, it.resolution)
            result.add(myVideo)
        }

//        val listRecent = filterRecentVideo(result)
        return Resource.Success(result)
    }
    private fun filterRecentVideo(videos: List<MyVideo>): ArrayList<MyFolderVideo> {
        val listFolderVideo = ArrayList<MyFolderVideo>()
        for (video in videos) {
            val pos = checkFolderVideoByTime(video, listFolderVideo)
            if (pos >= 0) {
                listFolderVideo[pos].list.add(video)
            } else {
                val file = File(video.myFile!!.data!!)
                val folder = MyFile(
                    getNameFolderByTime(file.lastModified()),
                    getNameFolderByTime(file.lastModified()),
                    "",
                    file.length(),
                    file.lastModified(),
                    file.lastModified(),
                    file.parentFile?.path ?: ""
                )
                val folderVideo = MyFolderVideo(folder)
                folderVideo.list.add(video)

                listFolderVideo.add(folderVideo)
            }
        }
        return listFolderVideo
    }

    private fun checkFolderVideoByTime(
        video: MyVideo,
        listFolderVideo: ArrayList<MyFolderVideo>
    ): Int {
        for (i in listFolderVideo.indices) {
            if (getNameFolderByTime(video.myFile!!.dateModified) == listFolderVideo[i].folder.title) {
                return i
            }
        }
        return -1
    }
    fun checkFavoriteFolderSoundAndRemove(item: SoundFolderPrank): Boolean {
        var listSoundFolder = Hawk.get<ArrayList<SoundFolderPrank>>(LIST_FOLDER_SOUND_FAVORITE, ArrayList())
        val itr = listSoundFolder.listIterator()

        while (itr.hasNext()) {

            if (listSoundFolder.size == 0) {
                return false
            } else {
                var index = itr.nextIndex()
                if (itr.next().id==item.id) {
                    listSoundFolder.removeAt(index)
                    Hawk.put<java.util.ArrayList<SoundFolderPrank>>(LIST_FOLDER_SOUND_FAVORITE, listSoundFolder)
                    return true
                }
            }
        }
        return false
    }
}

