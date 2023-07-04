package com.example.myapplication6.ui.address

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.icu.text.Transliterator.Position
import android.os.Bundle
import android.text.Editable
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.net.toUri
import com.example.myapplication6.MyApplication
import com.example.myapplication6.databinding.DialogBinding
import com.example.myapplication6.ui.gallery.Image
import com.google.gson.Gson
import java.text.FieldPosition

class ProfileDialog(
    context: Context,
    profile: Profile,
    position: Int,
    profileList: ArrayList<Profile>,
    adapter: AddressAdapter,
    private val okCallback: (Profile) -> Unit
    ): Dialog(context) { // 뷰를 띄워야하므로 Dialog 클래스는 context를 인자로 받는다.

    private lateinit var binding: DialogBinding
    var profile: Profile = profile
    var position: Int  = position



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        // dialog window 속성 가져오기
        val window = window
        val layoutParams = WindowManager.LayoutParams().apply {
            copyFrom(window?.attributes)
            width = WindowManager.LayoutParams.MATCH_PARENT // 프래그먼트의 너비로 설정하고자하는 값으로 변경
            height = WindowManager.LayoutParams.MATCH_PARENT
        }
        window?.attributes = layoutParams

        // 만들어놓은 dialog_profile.xml 뷰를 띄운다.
        binding = DialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.name.setText(profile.name)
        binding.phone.setText(profile.phone)
        binding.image.setImageURI(profile.img.toUri())

        binding.kaist.prompt = profile.kaist
        binding.male.prompt = profile.male

        binding.buttonDialog.setOnClickListener {
            if (binding.name.text.isNullOrBlank()  ) {
                Toast.makeText(context, "이름을 입력하세요!", Toast.LENGTH_SHORT).show()
            } else if(binding.phone.text.isNullOrBlank()) {
                Toast.makeText(context, "전화번호를 입력하세요!", Toast.LENGTH_SHORT).show()
            }else{
                val modifiedProfile = Profile(
                    profile.img,
                    binding.name.text.toString(),
                    profile.age,
                    binding.phone.text.toString(),
                    binding.male.selectedItem.toString(),
                    binding.kaist.selectedItem.toString()
                )
                this.profile = modifiedProfile
                okCallback(modifiedProfile)
                dismiss()
            }
        }

        //이미지 클릭 시 로드
        // 이미지 저장.

        binding.image.setOnClickListener{
            val intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            //getResult.launch(intent)
        }
        fun arrayToGson(arrayList: ArrayList<Image>):String{
            val gson = Gson()
            val jsonString = gson.toJson(arrayList)
            println(jsonString)
            return jsonString
        }
    }






}