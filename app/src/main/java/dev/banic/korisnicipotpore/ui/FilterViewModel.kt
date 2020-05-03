package dev.banic.korisnicipotpore.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class FilterViewModel : ViewModel() {
    var filter: MutableLiveData<CharSequence> = MutableLiveData<CharSequence>()
}
