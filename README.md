# MaskedTextWatcher

A TextWatcher that formats the EditText content using a mask

# How to use
Just call MaskedTextWatcher.addWatcher(mask, editText) passing your desired mask and the editText that will use it.


# Know issues
- For now it only supports '#' for as the variable on the mask
- Mask can't have chars that can be entered by the user
