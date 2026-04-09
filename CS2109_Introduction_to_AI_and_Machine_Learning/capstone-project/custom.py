import os

from PIL import Image, ImageDraw

# NOTE: Taken from https://github.com/rizkiarm/grid-universe/blob/main/grid_universe/utils/image.py
def draw_direction_triangles_on_image(
    image: Image.Image, size: int, dx: int, dy: int, count: int
) -> Image.Image:
    """
    Draw 'count' filled triangles pointing (dx, dy) on the given RGBA image.
    Triangles are centered: the centroid of each triangle is symmetrically arranged
    around the image center. Spacing is between triangle centroids.
    """
    if count <= 0 or (dx, dy) == (0, 0):
        return image

    draw = ImageDraw.Draw(image)
    cx, cy = size // 2, size // 2

    # Triangle geometry (relative to size)
    tri_height = max(4, int(size * 0.16))
    tri_half_base = max(3, int(size * 0.10))
    spacing = max(2, int(size * 0.12))  # distance between triangle centroids

    # Axis-aligned direction and perpendicular
    ux, uy = dx, dy  # points toward the triangle tip
    px, py = -uy, ux  # perpendicular (for base width)

    # Offsets for centroids: 1 -> [0], 2 -> [-0.5s, +0.5s], 3 -> [-s, 0, +s], ...
    offsets = [(i - (count - 1) / 2.0) * spacing for i in range(count)]

    # For an isosceles triangle, the centroid lies 1/3 of the height from the base toward the tip.
    # If C is the centroid, then:
    #   tip = C + (2/3)*tri_height * u
    #   base_center = C - (1/3)*tri_height * u
    tip_offset = (2.0 / 3.0) * tri_height
    base_offset = (1.0 / 3.0) * tri_height

    for off in offsets:
        # Centroid position
        Cx = cx + int(round(ux * off))
        Cy = cy + int(round(uy * off))

        # Tip and base-center positions
        tip_x = int(round(Cx + ux * tip_offset))
        tip_y = int(round(Cy + uy * tip_offset))
        base_x = int(round(Cx - ux * base_offset))
        base_y = int(round(Cy - uy * base_offset))

        # Base vertices around base center along the perpendicular
        p1 = (tip_x, tip_y)
        p2 = (
            int(round(base_x + px * tri_half_base)),
            int(round(base_y + py * tri_half_base)),
        )
        p3 = (
            int(round(base_x - px * tri_half_base)),
            int(round(base_y - py * tri_half_base)),
        )

        draw.polygon([p1, p2, p3], fill=(255, 255, 255, 220), outline=(0, 0, 0, 220))

    return image


# Generate permutation of all triangles on all images in the folder
def draw_triangle(base_folder: str):
    directions = {
        "up": (0, -1),
        "down": (0, 1),
        "left": (-1, 0),
        "right": (1, 0)
    }

    for dir, (dx, dy) in directions.items():
        output_folder = base_folder + "_" + dir
        os.makedirs(output_folder, exist_ok=True)

        for filename in os.listdir(base_folder):
            raw_img_path = os.path.join(base_folder, filename)
            raw_img = Image.open(raw_img_path).convert("RGBA")

            result = draw_direction_triangles_on_image(raw_img, 128, dx, dy, 1)

            # result.show()
            # break

            output_path = os.path.join(output_folder, filename)
            result.save(output_path)


# Generate permutation of all background + entities (which you can see the background through)
def overlay_background(entity_folder: str, background_folder: str, output_folder: str):
    for label_folder_name in os.listdir(entity_folder):
        label_folder_path = os.path.join(entity_folder, label_folder_name)
        output_folder_path = os.path.join(output_folder, label_folder_name)
        os.makedirs(output_folder_path, exist_ok=True)

        for entity_file_name in os.listdir(label_folder_path):
            entity_path = os.path.join(label_folder_path, entity_file_name)
            entity_image = Image.open(entity_path).convert("RGBA")

            for background_file_name in os.listdir(background_folder):
                background_path = os.path.join(background_folder, background_file_name)
                background_image = Image.open(background_path).convert("RGBA").resize(entity_image.size)

                overlaid_image = Image.alpha_composite(background_image, entity_image)
                
                entity_name = os.path.splitext(entity_file_name)[0]
                background_name = os.path.splitext(background_file_name)[0]
                output_file_name = f"{entity_name}_on_{background_name}.png"
                output_file_path = os.path.join(output_folder_path, output_file_name)

                overlaid_image.save(output_file_path)


if __name__ == "__main__":
    # draw_triangle("data/assets/imagen1/metalbox")
    # draw_triangle("data/assets/imagen1/robot")

    overlay_background("data/entity", "data/floor", "data/direction_w_bg")

    exit

