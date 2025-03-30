# Formatear USB desde Ubuntu

* Ver la unidad
  
```shell
df 
```

* Desmontar

```shell
sudo umount /dev/sda1
```

* Formatear

```shell
Para el sistema de archivos vFAT (FAT32): sudo mkfs.vfat / dev / sdc1
Para el sistema de archivos NTFS: sudo mkfs.ntfs / dev / sdc1
Para el sistema de archivos EXT4: sudo mkfs.ext4 / dev / sdc1
```
