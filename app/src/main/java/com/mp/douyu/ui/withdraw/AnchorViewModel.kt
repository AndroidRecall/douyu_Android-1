package com.mp.douyu.ui.withdraw

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.blankj.utilcode.util.GsonUtils
import com.mp.douyu.base.MBaseViewModel
import com.mp.douyu.bean.*
import com.mp.douyu.http.DisposableSubscriberAdapter
import com.mp.douyu.http.NobodyConverterFactory
import com.mp.douyu.http.Respository
import com.mp.douyu.utils.ToastUtils
import org.json.JSONObject

class AnchorViewModel : MBaseViewModel() {

    private val allAnchorInfoData = MutableLiveData<HashMap<String, String?>>()
    private val allWithdrawData = MutableLiveData<HashMap<String, String?>>()
    private val allApplyStateData = MutableLiveData<HashMap<String, String?>>()
    private val allAdvListData = MutableLiveData<HashMap<String, String?>>()
    private val allAccountData = MutableLiveData<HashMap<String, String?>>()
    private val allAddAccountData = MutableLiveData<HashMap<String, String?>>()
    private val allWithdrawRecordData = MutableLiveData<HashMap<String, String?>>()
    private val allRecordDetailData = MutableLiveData<HashMap<String, String?>>()


    val _anchorInfoData = Transformations.switchMap(allAnchorInfoData) {
        val mutableLiveData = MutableLiveData<AnchorBean>()
        Respository.getAnchorInfo(it,
            object :DisposableSubscriberAdapter<AnchorBean>(this){
                override fun onNext(t: AnchorBean) {
                    mutableLiveData.value = t
                }
                override fun onError(t: Throwable?) {
                    super.onError(t)
                    mutableLiveData.value = mutableLiveData.value
                }
            }
         )
        mutableLiveData
    }

    fun getAnchorInfo(hashMap: HashMap<String, String?>) {
        allAnchorInfoData.value = hashMap
    }
    val _withdrawResult = Transformations.switchMap(allWithdrawData) {
        val mutableLiveData = MutableLiveData<WithdrawDetailRes>()
        Respository.withdraw(it, object : DisposableSubscriberAdapter<NobodyConverterFactory.NoBodyEntity>(this) {
            override fun onNext(t: NobodyConverterFactory.NoBodyEntity?) {
                t?.let {
                    val result = JSONObject((it as NobodyConverterFactory.NoBodyEntity).json)
                    val code = result.getInt("status")
                    val message = result.getString("message")
                    val data = result.getString("data")
                    if (code == 200) {
                        ToastUtils.showToast(message)
                        val result = GsonUtils.fromJson(data, WithdrawDetailRes::class.java)
                        mutableLiveData.value = result

                    } else {
                        ToastUtils.showToast(message)
                    }
                }
            }
            override fun onError(t: Throwable?) {
                super.onError(t)
                ToastUtils.showToast("${t?.localizedMessage}")
            }

        })
        mutableLiveData
    }

    fun withdraw(hashMap: HashMap<String, String?>) {
        allWithdrawData.value = hashMap
    }
    val _getAdvList = Transformations.switchMap(allAdvListData) {
        val mutableLiveData = MutableLiveData<List<AdvBean?>>()
        Respository.getAdvList(it,
            object : DisposableSubscriberAdapter<List<AdvBean?>>(this) {
                override fun onNext(t: List<AdvBean?>) {
                    mutableLiveData.value = t
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                    mutableLiveData.value = mutableLiveData.value
                }
            })
        mutableLiveData
    }

    fun getAdvList(hashMap: HashMap<String, String?>) {
        allAdvListData.value = hashMap
    }
    val _accountList = Transformations.switchMap(allAccountData) {
        val mutableLiveData = MutableLiveData<List<AccountRes>>()
        Respository.getAccountRecord(it,
            object : DisposableSubscriberAdapter<List<AccountRes>>(this) {
                override fun onNext(t: List<AccountRes>) {
                    mutableLiveData.value = t
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                    mutableLiveData.value = mutableLiveData.value
                }
            })
        mutableLiveData
    }

    fun getAccount(hashMap: HashMap<String, String?>) {
        allAccountData.value = hashMap
    }

    val _ruleList = Transformations.switchMap(allAccountData) {
        val mutableLiveData = MutableLiveData<List<AnchorRuleBean>>()
        Respository.getAnchorLvRules(it,
            object : DisposableSubscriberAdapter<List<AnchorRuleBean>>(this) {
                override fun onNext(t: List<AnchorRuleBean>) {
                    mutableLiveData.value = t
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                    mutableLiveData.value = mutableLiveData.value
                }
            })
        mutableLiveData
    }
    fun getRule(hashMap: HashMap<String, String?>) {
        allAccountData.value = hashMap
    }
    val _addAccountResult = Transformations.switchMap(allAddAccountData) {
        val mutableLiveData = MutableLiveData<AccountRes>()
        Respository.addWithdrawAccount(it,
            object : DisposableSubscriberAdapter<AccountRes>(this) {
                override fun onNext(t: AccountRes) {
                    mutableLiveData.value = t
                    ToastUtils.showToast("添加账号成功")
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                    ToastUtils.showToast("${t?.localizedMessage}")
                    mutableLiveData.value = mutableLiveData.value
                }
            })
        mutableLiveData
    }

    fun addAccount(hashMap: HashMap<String, String?>) {
        allAddAccountData.value = hashMap
    }
    val _withdrawRecordList = Transformations.switchMap(allWithdrawRecordData) {
        val mutableLiveData = MutableLiveData<HttpDataListBean<WithdrawRecordBean>>()
        Respository.getWithdrawRecord(it,
            object : DisposableSubscriberAdapter<HttpDataListBean<WithdrawRecordBean>>(this) {
                override fun onNext(t: HttpDataListBean<WithdrawRecordBean>) {
                    mutableLiveData.value = t
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                    ToastUtils.showToast("${t?.localizedMessage}")
                    mutableLiveData.value = mutableLiveData.value
                }
            })
        mutableLiveData
    }

    fun getRecord(hashMap: HashMap<String, String?>) {
        allWithdrawRecordData.value = hashMap
    }
    val _recordDetail = Transformations.switchMap(allRecordDetailData) {
        val mutableLiveData = MutableLiveData<WithdrawDetailRes>()
        Respository.getWithdrawInfo(it,
            object : DisposableSubscriberAdapter<WithdrawDetailRes>(this) {
                override fun onNext(t: WithdrawDetailRes) {
                    mutableLiveData.value = t
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                    ToastUtils.showToast("${t?.localizedMessage}")
                    mutableLiveData.value = mutableLiveData.value
                }
            })
        mutableLiveData
    }

    fun getRecordDetail(hashMap: HashMap<String, String?>) {
        allRecordDetailData.value = hashMap
    }

    val _applyResult = Transformations.switchMap(allApplyStateData) {
        val mutableLiveData = MutableLiveData<String>()
        Respository.getAnchorApplyStatus(it, object : DisposableSubscriberAdapter<NobodyConverterFactory.NoBodyEntity>(this) {
            override fun onNext(t: NobodyConverterFactory.NoBodyEntity?) {
                t?.let {
                    try {
                        val result = JSONObject((it as NobodyConverterFactory.NoBodyEntity).json)
                        val code = result.getInt("status")
                        val message = result.getString("message")
                        val data = result.getString("data")
                            mutableLiveData.value = data
                        if (code == 200) {
                        } else {
//                            ToastUtils.showToast(message)
                        }
                    } catch (e: Exception) {
                        ToastUtils.showToast(e.localizedMessage)
                    }
                }
            }
            override fun onError(t: Throwable?) {
                super.onError(t)
                ToastUtils.showToast("${t?.localizedMessage}")
            }

        })
        mutableLiveData
    }

    fun getApplyState(hashMap: HashMap<String, String?>) {
        allApplyStateData.value = hashMap
    }
}