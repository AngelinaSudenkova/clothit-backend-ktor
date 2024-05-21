package com.clothit.error

sealed class CustomException(exceptionType: ErrorTypes, message: String) : Exception(message) {
    class FileNotFoundException() : CustomException(ErrorTypes.NOT_FOUND_EXCEPTION, "File can not be found")
    class FileCanNotBeSavedException() : CustomException(ErrorTypes.INTERNAL_SERVER_ERROR, "File can not be saved")
    class OutfitCanNotBeSavedException() : CustomException(ErrorTypes.INTERNAL_SERVER_ERROR, "Outfit can not be saved")
    class ItemCanNotBeSavedException() : CustomException(ErrorTypes.INTERNAL_SERVER_ERROR, "Item can not be saved")
    class UpdateException() : CustomException(ErrorTypes.INTERNAL_SERVER_ERROR, "An error occurred while updating")
    class ItemNotFoundException(): CustomException(ErrorTypes.NOT_FOUND_EXCEPTION, "Item can not be found")
    class OutfitNotFoundException(): CustomException(ErrorTypes.NOT_FOUND_EXCEPTION, "Outfit can not be found")

}