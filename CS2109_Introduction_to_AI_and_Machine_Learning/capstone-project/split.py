import os
import shutil
import random

src = "data/direction_w_bg"
train_dst = "data/train"
val_dst = "data/val"
test_dst = "data/test"

random.seed(42)

for class_name in os.listdir(src):
    class_path = os.path.join(src, class_name)
    if not os.path.isdir(class_path):
        continue

    images = os.listdir(class_path)
    random.shuffle(images)
    n_val = int(n * 0.20)
    test_images = images[:n_test]
    train_images = images[n_test:]

    for dst, split in [(train_dst, train_images), (test_dst, test_images)]:
        os.makedirs(os.path.join(dst, class_name), exist_ok=True)
        for img in split:
            shutil.copy(
                os.path.join(class_path, img),
                os.path.join(dst, class_name, img)
            )

print("Done splitting!")