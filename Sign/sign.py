import os
import subprocess
import shutil


# 复制文件
def copy_file(src_file, dst_file):
    if not os.path.isfile(src_file):
        print("%s not exist" % src_file)
    else:
        file_path, file_name = os.path.split(dst_file)  # 分离文件名和路径
        if not os.path.exists(file_path):
            os.makedirs(file_path)  # 创建路径
        shutil.copyfile(src_file, dst_file)  # 复制文件
        print("copy %s -> %s" % (src_file, dst_file))


pathUnSignFile = "sign"  # 待签名文件目录
pathSignedFile = "out"  # 签完名输出目录
pathApkSigner = "E:\DevTool\\androidStudio\SDK\\build-tools\\26.0.1\\apksigner"  # apksigner 路径
pathCheckV2Sign = "E:\Project\CecPay\\1_ShankPhone\Doc\\16_v2sign\CheckAndroidV2Signature.jar"  # 检查V2签名是否成功
pathZipAlign = "E:\DevTool\\androidStudio\SDK\\build-tools\\26.0.1\\zipalign"  # zipalign 路径

BASE_DIR = os.path.dirname(__file__)
print('BASE_DIR---' + BASE_DIR)
isUnSignFilePathExist = os.path.exists(pathUnSignFile)
print("isSignFilePathExist", isUnSignFilePathExist)
if not isUnSignFilePathExist:
    os.mkdir(pathUnSignFile)

fieList = os.listdir(os.path.join(BASE_DIR, pathUnSignFile))
print("fieListSize=", len(fieList))
for file in fieList:
    print("file name = ", file)
    orgFile = os.path.join(os.path.join(BASE_DIR, pathUnSignFile), file)
    print("orgFilePath  = ", orgFile)
    prefix_file_name = file[0:len(file) - 4]
    print("prefix_file_name = ", prefix_file_name)
    newFile = os.path.join(os.path.join(BASE_DIR, pathSignedFile), file)
    print("newFilePath= ", newFile)
    copy_file(orgFile, newFile)

    keystore = 'E:\Project\CecPay\\1_ShankPhone\Keystore\Release\shankphone.keystore'
    key_pass = 'cssweb888888'
    key_alias = 'shankphone_keystore'  # print("keystore = ", os.path.split(keystore)[0])

    prefix_file_name = newFile[0:len(newFile) - 4]
    zipAlignFile = os.path.join(os.path.join(BASE_DIR, pathSignedFile), prefix_file_name + "_align.apk")
    # zipalign
    alignCmd = pathZipAlign + ' -v 4 "%s" "%s"' % (newFile, zipAlignFile)
    print("align file name= " + zipAlignFile)
    result = subprocess.getoutput(alignCmd)
    print(result)
    os.remove(newFile)
    # v2签名
    v2SignCmd = pathApkSigner + ' sign --ks %s --ks-pass pass:%s --ks-key-alias %s %s' % (
        keystore, key_pass, key_alias, zipAlignFile)
    # result = os.popen(v2SignCmd)
    # print(result)
    result = subprocess.getoutput(v2SignCmd)
    print(result)
    prefix_file_name = zipAlignFile[0:len(zipAlignFile) - 4]
    signFile = os.path.join(os.path.join(BASE_DIR, pathSignedFile), prefix_file_name + "_v2sign.apk")
    if os.path.exists(signFile):
        os.remove(signFile)
    os.rename(zipAlignFile, signFile)

    checkV2SignCmd = "java -jar " + pathCheckV2Sign + " " + signFile
    result2 = subprocess.getoutput(checkV2SignCmd)
    print(result2)

# print(file + " finish\n")
