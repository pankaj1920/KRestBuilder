package com.app.krestbuilder.utils.filemanager

object FileDirectory {
    val KOIN_DI_PATH = "di"
    val DOMAIN_PATH = "domain"
    val MAPPER_PATH = "${DOMAIN_PATH}/mapper"
    val MODEL_PATH = "${DOMAIN_PATH}/model"
    val REPOSITORY_PATH = "${DOMAIN_PATH}/repository"
    val USE_CASE_PATH = "${DOMAIN_PATH}/usecase"
    val REMOTE_PATH = "remote"
    val DTO_PATH = "${REMOTE_PATH}/dto"
    val RESPONSE_DTO_PATH = "$DTO_PATH"
    val REQUEST_BODY_PATH = "$DTO_PATH"
    val API_SERVICE_PATH = "$REMOTE_PATH"
}