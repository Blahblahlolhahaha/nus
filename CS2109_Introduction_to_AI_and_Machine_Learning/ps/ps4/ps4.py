# RUN THIS CELL FIRST
import math
from collections import OrderedDict

import matplotlib.pyplot as plt
import torch
import torch.nn as nn
import torch.nn.functional as F
from torch.utils.data import TensorDataset, DataLoader
from torchvision import datasets, transforms
from torchvision.transforms import v2

import numpy as np
from numpy import allclose, isclose

from collections.abc import Callable

device = "cuda" if torch.cuda.is_available() else "cpu"
print(device)

# Define a linear layer using nn.Module
class LinearLayer(nn.Module):
    """
    Linear layer as a subclass of `nn.Module`.
    """
    def __init__(self, input_dim: int, output_dim: int):
        super().__init__()
        self.weight = nn.Parameter(torch.randn(input_dim, output_dim))
        self.bias = nn.Parameter(torch.randn(output_dim))

    def forward(self, x: torch.Tensor) -> torch.Tensor:
        return torch.matmul(x, self.weight) + self.bias

class SineActivation(nn.Module):
    """
    Sine activation layer.
    """
    def __init__(self):
        super().__init__()

    def forward(self, x: torch.Tensor) -> torch.Tensor:
        return torch.sin(x)

class Model(nn.Module):
    """
    Neural network created using `LinearLayer` and `SineActivation`.
    """
    def __init__(self, input_size: int, hidden_size: int, output_size: int):
        super(Model, self).__init__()
        self.l1 = LinearLayer(input_size, hidden_size)
        self.act = SineActivation()
        self.l2 = LinearLayer(hidden_size, output_size)

    def forward(self, x: torch.Tensor) -> torch.Tensor:
        x = self.l1(x)
        x = self.act(x)
        x = self.l2(x)
        return x
    
input_size = 1
hidden_size = 2
output_size = 1

model = Model(input_size, hidden_size, output_size)

x = torch.tensor([[1.0]])
output = model(x)
print("Original value: ", x)
print("Value after being processed by Model: ", output)

x_sample = torch.linspace(-2, 2, 100)
sigmoid_output = nn.Sigmoid()(x_sample).detach().numpy()
tanh_output = nn.Tanh()(x_sample).detach().numpy()
relu_output = nn.ReLU()(x_sample).detach().numpy()

f = plt.figure()
f.set_figwidth(6)
f.set_figheight(6)
plt.xlabel('x - axis')
plt.ylabel('y - axis')
plt.title("Input: 100 x-values between -2 to 2 \n\n Output: Corresponding y-values after passed through each activation function\n", fontsize=16)
plt.axvline(x=0, color='r', linestyle='dashed')
plt.axhline(y=0, color='r', linestyle='dashed')
plt.plot(x_sample, sigmoid_output)
plt.plot(x_sample, tanh_output)
plt.plot(x_sample, relu_output)
plt.legend(["","","Sigmoid Output", "Tanh Output", "ReLU Output"])
plt.show()

# Suppose we had an nn.Parameter x (or really, any tensor with requires_grad=True)
x = torch.tensor([1.0], requires_grad=True)

# We would like to update it with SGD, so we create an optimiser for it.
optimiser = torch.optim.SGD([x], lr=0.1)

# Hence, we compute some random function (usually, this is your loss function) in x...
y = x ** 2 + 2 * x

# And start the backpropagation process:
# Compute the gradients of y with respect to x, and store it in x.grad
y.backward()

print("Value of x before it is updated by optimiser: ", x)
print("Gradient stored in x after backpropagation: ", x.grad)

# Make the optimizer update x based on x.grad.
optimiser.step()

# You should expect to see x = x - lr * x.grad = 1.0 - 0.1 * 4.0 = 0.60
print("Value of x after it is updated by optimiser: ", x)

# Now that gradients are no longer needed, we discard them by setting them to zero.
optimiser.zero_grad()
print("Gradient stored in x after zero_grad is called: ", x.grad)

# Run to view your model weights
print(model.state_dict())

x = torch.tensor([[1.0]], requires_grad=True)

# This will throw an error as x is still part of the computational graph, and cannot be converted to a NumPy array.
try:
    x_numpy = x.numpy()
except Exception as e:
    print("Error: ", e)

# You should expect to successfuly convert x to a NumPy array after detaching 
# it from the computational graph and moving it to the CPU (if necessary).
x_numpy = x.detach().numpy() # You may need to additionally call .cpu() if you are using a GPU, i.e. x.detach().cpu().numpy()
print("Successfully converted x to a NumPy array: ", x_numpy)

# DO NOT REMOVE THIS CELL – THIS DOWNLOADS THE MNIST DATASET
# RUN THIS CELL BEFORE YOU RUN THE REST OF THE CELLS BELOW

# This downloads the MNIST datasets ~63MB
mnist_train = datasets.MNIST("./", train=True, download=True)
mnist_test  = datasets.MNIST("./", train=False, download=True)

x_train = mnist_train.data.reshape(-1, 784) / 255
y_train = mnist_train.targets
    
x_test = mnist_test.data.reshape(-1, 784) / 255
y_test = mnist_test.targets

### Task 1.1 - Define the model architecture and implement the forward pass

class DigitNet(nn.Module):
    def __init__(self, input_dimensions: int, num_classes: int): # set the arguments you'd need
        super().__init__()
        """
        YOUR CODE HERE
        - DO NOT hardcode the input_dimensions, use the parameter in the function
        - Your network should work for any input and output size 
        - Create the 3 layers (and a ReLU layer) using the torch.nn layers API
        """
        """ YOUR CODE HERE """
        raise NotImplementedError
        """ YOUR CODE END HERE """
        
    def predict_proba(self, x: torch.Tensor) -> torch.Tensor:
        """
        
        Same as the forward pass, except this outputs probabilities 
        for each class for each input, instead of the predicted class.

        Parameters
        ----------
        x : Input tensor (batch size is the entire dataset)

        Returns
        -------
            The output probabilities for each class for each input.
        """
        
        out = self.forward(x)
        return torch.softmax(out, dim=1)

    def forward(self, x: torch.Tensor) -> torch.Tensor:
        """
        Performs the forward pass for the network.
        
        Parameters
        ----------
        x : Input tensor (batch size is the entire dataset)

        Returns
        -------
            The output of the entire 3-layer model.
        """
        
        """
        YOUR CODE
        
        - Pass the inputs through the sequence of layers
        - Run the final output through the Softmax function on the right dimension!
        """
        """ YOUR CODE HERE """
        raise NotImplementedError
        """ YOUR CODE END HERE """

def test_task_1_1():
    model = DigitNet(784, 10)
    assert [layer.detach().numpy().shape for name, layer in model.named_parameters()] \
            == [(512, 784), (512,), (128, 512), (128,), (10, 128), (10,)]

# Run this code before moving on.
# DO NOT EDIT!
train_set = TensorDataset(x_train, y_train)
test_set = TensorDataset(x_test, y_test)

train_loader = DataLoader(train_set, shuffle=True, batch_size = len(train_set))
# We don't shuffle the test set; we don't learn from it anyway, that effort is wasted.
test_loader = DataLoader(test_set, shuffle=False, batch_size = len(test_set))

### Task 1.2 - Training Loop

def train_model(model: nn.Module, dataloader: DataLoader, epochs: int = 20):
    """
    Trains the model for a specified number of epochs/iterations
    
    Parameters
    ---------- 
        model: A PyTorch model to be trained
        dataloader : A DataLoader object that provides batches of the training data
        epochs  : Number of epochs, default of 20
        
    Returns
    -------
        The final model and the loss curve (per epoch)
    """

    losses = []

    """ YOUR CODE HERE """
    raise NotImplementedError
    """ YOUR CODE END HERE """

    # Set model to training mode. 
    # See (https://stackoverflow.com/questions/60018578/what-does-model-eval-do-in-pytorch) if curious.
    model.train() 
    for i in range(epochs):
        for x_batch, y_batch in dataloader:
            epoch_loss = 0.0
            """ YOUR CODE HERE """
            raise NotImplementedError
            """ YOUR CODE END HERE """
            epoch_loss += loss.item()
        print(f"Epoch {i+1}/{epochs}, Loss: {epoch_loss:.4f}")
        losses.append(epoch_loss)

    return model, losses

def test_task_1_2():
    x_train_new = torch.rand(5, 784, requires_grad=True)
    y_train_new = torch.ones(5, dtype=torch.uint8)
    
    train_set_new = TensorDataset(x_train_new, y_train_new)
    
    train_loader_new = DataLoader(train_set_new, shuffle=True, batch_size = len(train_set_new))
    
    
    assert isinstance(train_model(DigitNet(784, 10), train_loader_new)[0], DigitNet)

# This is a demonstration: You can use this cell for exploring your trained model

idx = 0 # try on some index

scores = digit_model(x_test[idx:idx+1])
_, predictions = torch.max(scores, 1)
print("true label:", y_test[idx].item())
print("pred label:", predictions[0].item())

plt.imshow(x_test[idx].numpy().reshape(28, 28), cmap='gray')
plt.axis("off")
plt.show()

### Task 1.3 - Evaluate the Model

def get_accuracy(scores: torch.Tensor, labels: torch.Tensor) -> int | float:
    """
    Helper function that returns accuracy of model
    
    Parameters
    ----------
        scores : The raw softmax scores of the network
        labels : The ground truth labels
        
    Returns
    -------
        Accuracy of the model. Return a number in range [0, 1].
        0 means 0% accuracy while 1 means 100% accuracy
    """
    """ YOUR CODE HERE """
    raise NotImplementedError
    """ YOUR CODE END HERE """

def test_task_1_3():
    scores = torch.tensor([[0.4118, 0.6938, 0.9693, 0.6178, 0.3304, 0.5479, 0.4440, 0.7041, 0.5573,
             0.6959],
            [0.9849, 0.2924, 0.4823, 0.6150, 0.4967, 0.4521, 0.0575, 0.0687, 0.0501,
             0.0108],
            [0.0343, 0.1212, 0.0490, 0.0310, 0.7192, 0.8067, 0.8379, 0.7694, 0.6694,
             0.7203],
            [0.2235, 0.9502, 0.4655, 0.9314, 0.6533, 0.8914, 0.8988, 0.3955, 0.3546,
             0.5752],
            [0,0,0,0,0,0,0,0,0,1]])
    y_true = torch.tensor([5, 3, 6, 4, 9])
    acc_true = 0.4
    assert isclose(get_accuracy(scores, y_true),acc_true) , "Mismatch detected"
    print("passed")

# Save your MNIST model for later use!
torch.save(digit_model.state_dict(), "task1")

# Load from the saved model state dict
new_digit_model = DigitNet(784, 10)
new_digit_model.load_state_dict(torch.load("task1"))

# do not remove this cell
# run this before moving on

T = transforms.Compose([
    transforms.ToTensor(),
    transforms.Normalize([0.5], [0.5])
])

"""
Note: If you updated the path to the directory containing `MNIST` 
directory, please update it here as well.
"""
mnist_train = datasets.MNIST("./", train=True, download=False, transform=T)
mnist_test = datasets.MNIST("./", train=False, download=False, transform=T)

"""
if you feel your computer can't handle too much data, you can reduce the batch
size to 64 or 32 accordingly, but it will make training slower. 

We recommend sticking to 128 but do choose an appropriate batch size that your
computer can manage. The training phase tends to require quite a bit of memory.
"""
train_loader = DataLoader(mnist_train, shuffle=True, batch_size=256)
test_loader = DataLoader(mnist_test, batch_size=10000)

# No need to edit this. Just run the cell and move on

train_features, train_labels = next(iter(train_loader))
print(f"Feature batch shape: {train_features.size()}")
print(f"Labels batch shape: {train_labels.size()}")
img = train_features[0].squeeze()
label = train_labels[0]
plt.imshow(img, cmap="gray")
plt.axis("off")
plt.show()
print(f"Label: {label}")

# Demo
class RawCNN(nn.Module):
    """
    CNN model using Conv2d and MaxPool2d layers.
    """
    def __init__(self, classes: int):
        super().__init__()
        """
        classes: integer that corresponds to the number of classes for MNIST
        """
        # Convolution Layer for 1 input channel, 32 filters, and a 3x3 kernel size
        self.conv1 = nn.Conv2d(1, 32, (3,3))
        # MaxPool layer with kernel size 2x2
        self.mp1 = nn.MaxPool2d((2,2))

        # [LeakyReLU](https://pytorch.org/docs/stable/generated/torch.nn.LeakyReLU.html) with negative slope of 0.1
        # This is a common modification of ReLU to prevent the "dying ReLU" phenomenon, where neurons can get stuck and only output zero.
        self.lrelu = nn.LeakyReLU(0.1)

        # Try guessing what this does!
        self.conv2 = nn.Conv2d(32, 64, (3,3))
        self.mp2 = nn.MaxPool2d((2,2))

        self.l1 = nn.Linear(64*5*5, 256)
        self.l2 = nn.Linear(256, 128)
        self.l3 = nn.Linear(128, classes)
        
    def forward(self, x: torch.Tensor) -> torch.Tensor:
        # Warning: You need to add nonlinearities between each convolution layer,
        #          otherwise your combined layers will act identically to a single convolution layer!
        x = self.conv1(x)
        x = self.mp1(x)
        x = self.lrelu(x)

        x = self.conv2(x)
        x = self.mp2(x)
        x = self.lrelu(x)   
        
        x = x.view(-1, 64*5*5) # Flattening for our linear layers -- DO NOT REMOVE THIS LINE

        x = self.l1(x)
        x = self.lrelu(x)
        x = self.l2(x)
        x = self.lrelu(x)
        out = self.l3(x)
        
        return out
    
    def predict_proba(self, x: torch.Tensor) -> torch.Tensor:
        out = self.forward(x)
        return torch.softmax(out, dim = 1)
    
    def predict(self, x: torch.Tensor) -> torch.Tensor:
        out = self.predict_proba(x)
        _, predicted = torch.max(out, dim = 1)
        return predicted

# Test the network's forward pass
num_samples, num_channels, width, height = 20, 1, 28, 28
x = torch.rand(num_samples, num_channels, width, height)
net = RawCNN(10)
y = net(x)
print(y.shape) # torch.Size([20, 10])

### Task 2.1 - Building a ConvNet with Dropout

class DropoutCNN(nn.Module):
    """
    CNN that uses Conv2d, MaxPool2d, and Dropout layers.
    """
    def __init__(self, classes: int, drop_prob: float = 0.5):
        super().__init__()
        """
        classes: integer that corresponds to the number of classes for MNIST
        drop_prob: probability of dropping a node in the neural network
        """
        """ YOUR CODE HERE """
        raise NotImplementedError
        """ YOUR CODE END HERE """
        
    def forward(self, x: torch.Tensor) -> torch.Tensor:
        """ YOUR CODE HERE """
        raise NotImplementedError
        """ YOUR CODE END HERE """
        
        x = x.view(-1, 64*5*5) # Flattening – do not remove

        """ YOUR CODE HERE """
        raise NotImplementedError
        """ YOUR CODE END HERE """
    
    def predict_proba(self, x: torch.Tensor) -> torch.Tensor:
        out = self.forward(x)
        return torch.softmax(out, dim = 1)

def test_task_2_1():
    # Test your network's forward pass
    num_samples, num_channels, width, height = 20, 1, 28, 28
    x = torch.rand(num_samples, num_channels, width, height)
    net = DropoutCNN(10)
    y = net(x)
    print(y.shape) # torch.Size([20, 10])

%%time 
# do not remove the above line
print("======Training Vanilla Model======")
vanilla_model, vanilla_losses = train_model(RawCNN(10), train_loader,epochs = 10)
print("======Training Dropout Model======")
do_model, do_losses = train_model(DropoutCNN(10), train_loader, epochs = 10)

# do not remove – nothing to code here
# run this cell before moving on
# ensure get_accuracy from task 1.3 is defined

with torch.no_grad():
    vanilla_model.eval()
    for i, data in enumerate(test_loader):
        x, y = data
        x, y = x.to(device), y.to(device)
        pred_vanilla = vanilla_model.predict_proba(x)
        acc = get_accuracy(pred_vanilla, y)
        print(f"vanilla acc: {acc}")

    do_model.eval()
    for i, data in enumerate(test_loader):
        x, y = data
        x, y = x.to(device), y.to(device)
        pred_do = do_model.predict_proba(x)
        acc = get_accuracy(pred_do, y)
        print(f"drop-out (0.5) acc: {acc}")
        
"""
The network with Dropout might under- or outperform the network without
Dropout. However, in terms of generalisation, we are assured that the Dropout
network will not overfit – that's the guarantee of Dropout.

A very nifty trick indeed!
"""

### Task 2.2 - Observing Effects of Dropout

%%time 
# do not remove – nothing to code here
# run this before moving on

print("======Training Dropout Model with Dropout Probability 0.10======")
do10_model, do10_losses = train_model(DropoutCNN(10, 0.10), train_loader, 3)
print("======Training Dropout Model with Dropout Probability 0.95======")
do95_model, do95_losses = train_model(DropoutCNN(10, 0.95), train_loader, 3)

# do not remove – nothing to code here
# run this cell before moving on
# but ensure get_accuracy from task 3.5 is defined

with torch.no_grad():
    do10_model.eval()
    for i, data in enumerate(test_loader):
        x, y = data
        x, y = x.to(device), y.to(device)
        pred_do = do10_model.predict_proba(x)
        acc = get_accuracy(pred_do, y)
        print(acc)

    do95_model.eval()
    for i, data in enumerate(test_loader):
        x, y = data
        x, y = x.to(device), y.to(device)
        pred_do = do95_model.predict_proba(x)
        acc = get_accuracy(pred_do, y)
        print(acc)

densenet = nn.Sequential(
                nn.Linear(784, 512),
                nn.ReLU(),
                nn.Linear(512, 128),
                nn.ReLU(),
                nn.Linear(128, 10),
            )

with torch.no_grad(): # Turn off gradient tracking since we are only doing inference here
    x = torch.rand(15, 784) # a batch of 15 MNIST images
    y = torch.softmax(densenet(x), dim=1) # here we simply run the sequential densenet on the `x` tensor
    assert y.shape == (15, 10), f"Expected output shape of (15, 10) but got {y.shape}"
    assert torch.allclose(torch.sum(y, dim=1), torch.ones(15)), f"Softmax outputs do not sum to 1 across the label dimension! {torch.sum(y, dim = 1)}"

convnet = nn.Sequential(
                nn.Conv2d(1, 32, (3,3)),
                nn.ReLU(),
                nn.Conv2d(32, 64, (3,3)),
                nn.ReLU(),
                nn.Flatten(),
                nn.Linear(36864, 1024),
                nn.ReLU(),
                nn.Linear(1024, 512),
                nn.ReLU(),
                nn.Linear(512, 128),
                nn.ReLU(),
                nn.Linear(128, 10),
            )

with torch.no_grad(): # Turn off gradient tracking since we are only doing inference here
    x = torch.rand(15, 1, 28, 28) # a batch of 15 MNIST images
    y = torch.softmax(convnet(x), dim=1) # here we simply run the sequential convnet on the `x` tensor
    assert y.shape == (15, 10), f"Expected output shape of (15, 10) but got {y.shape}"
    assert torch.allclose(torch.sum(y, dim=1), torch.ones(15)), f"Softmax outputs do not sum to 1 across the label dimension! {torch.sum(y, dim=1)}"

cifar_train = datasets.CIFAR10("./", train=True, download=True, transform=v2.ToTensor())
cifar_train_loader = torch.utils.data.DataLoader(cifar_train, batch_size=128, shuffle=True)

train_features, train_labels = next(iter(cifar_train_loader))
img = train_features[0]

fig, (ax1, ax2) = plt.subplots(1, 2, figsize=(10,7))
transform = v2.Compose([v2.RandomHorizontalFlip()
                                # YOUR CODE HERE
                                ]) # Play with the transforms in https://docs.pytorch.org/vision/stable/transforms.html#v2-api-reference-recommended!
tensor_img = transform(img)
ax1.imshow(img.permute(1,2,0))
ax1.axis("off")
ax1.set_title("Before Transformation")
ax2.imshow(tensor_img.permute(1, 2, 0))
ax2.axis("off")
ax2.set_title("After Transformation")
plt.show()

### Task 2.3 - Picking Data Augmentations

def get_augmentations() -> transforms.Compose:
    T = transforms.Compose([
        transforms.ToTensor(),
        """ YOUR CODE HERE """
        raise NotImplementedError
        """ YOUR CODE END HERE """
    ])
    
    return T

def test_task_2_3():
    T = get_augmentations()
    
    x = torch.rand(32, 32, 3).numpy()
    x_t = T(x)
    x_to_tensor_only = transforms.ToTensor()(x)
    
    assert torch.is_tensor(x_t), "Output is not a tensor!"
    assert not torch.all(x_t == x_to_tensor_only), "Transforms do not do anything other than converting input to tensor!"
    assert x_t.shape == (3, 32, 32), f"Expected output shape of (3, 32, 32) but got {x_t.shape}"

class ShuffleChannels(nn.Module):
    def __init__(self):
        super().__init__()

    def forward(self, image):
        """
        Randomly permutes the channels of `image`.

        image: a tensor of shape (C, H, W) where C is the number of channels, 
               H is the height, and W is the width of the image.
        """
        permuted_indices = torch.randperm(image.shape[0])
        return image[permuted_indices]
    
# Example output of ShuffleChannels
train_features, train_labels = next(iter(cifar_train_loader))
img = train_features[0]

fig, (ax1, ax2) = plt.subplots(1, 2, figsize=(10,7))
shuffle = ShuffleChannels()
tensor_img = shuffle(img)

ax1.imshow(img.permute(1,2,0))
ax1.axis("off")
ax1.set_title("Before Transformation")
ax2.imshow(tensor_img.permute(1, 2, 0))
ax2.axis("off")
ax2.set_title("After Transformation")
plt.show()

### Task 2.4 - Write a Custom Data Augmentation

class SimulateRGBA(nn.Module):
    def __init__(self):
        super().__init__()
        # You may add any class attributes you need here

    def forward(self, image):
        """
        Adds a new channel to `image` that simulates an alpha channel.

        image: a tensor of shape (C, H, W) where C is the number of channels, 
               H is the height, and W is the width of the image, with values
               in the range [0, 1].
        """
        """ YOUR CODE HERE """
        raise NotImplementedError
        """ YOUR CODE END HERE """

def test_task_2_4():
    rand_input = torch.rand(3, 28, 28)
    simulate_rgba = SimulateRGBA()
    output = simulate_rgba(rand_input)
    
    assert output.shape == (4, 28, 28), f"Expected output shape of (4, 28, 28) but got {output.shape}"
    
    alpha_channel = output[3]
    assert torch.all((alpha_channel >= 0) & (alpha_channel <= 1)), "Values in the new channel should be in the range [0, 1]"
    assert not torch.all(alpha_channel == alpha_channel[0, 0]), "Values in the new channel should not be all the same"

# do not remove this cell
# run this before moving on

def get_augmentations() -> transforms.Compose:
    T = transforms.Compose([
        transforms.ToTensor(),
        # ShuffleChannels(), # Uncomment this line to test out the demo ShuffleChannels transformation
        SimulateRGBA(), 
        # --- You are free to experiment with more custom transformations! ---
        # Add in your data augmentations from Task 2.3 here
        transforms.Normalize([0.5, 0.5, 0.5, 0.5], [0.5, 0.5, 0.5, 0.5]),
        transforms.RandomHorizontalFlip()
    ])
    
    return T

T = get_augmentations()

cifar_train = datasets.CIFAR10("./", train=True, download=True, transform=T)
cifar_test = datasets.CIFAR10("./", train=False, download=True, transform=T)

"""
if you feel your computer can't handle too much data, you can reduce the batch
size to 64 or 32 accordingly, but it will make training slower. 

We recommend sticking to 128 but do choose an appropriate batch size that your
computer can manage. The training phase tends to require quite a bit of memory.

CIFAR-10 images have dimensions 3x32x32, while MNIST is 1x28x28
"""
cifar_train_loader = torch.utils.data.DataLoader(cifar_train, batch_size=128, shuffle=True)
cifar_test_loader = torch.utils.data.DataLoader(cifar_test, batch_size=10000)

### Task 2.5 - Build a ConvNet for CIFAR-10

class CIFARCNN(nn.Module):
    def __init__(self, classes: int):
        super().__init__()
        """
        classes: integer that corresponds to the number of classes for CIFAR-10
        """
        self.conv = nn.Sequential(
                        """ YOUR CODE HERE """
                        raise NotImplementedError
                        """ YOUR CODE END HERE """

                    )

        self.fc = nn.Sequential(
                        """ YOUR CODE HERE """
                        raise NotImplementedError
                        """ YOUR CODE END HERE """
                    )
        
    def forward(self, x: torch.Tensor) -> torch.Tensor:
        """ YOUR CODE HERE """
        raise NotImplementedError
        """ YOUR CODE END HERE """
        x = x.view(x.shape[0], 64, 6*6).mean(2) # GAP – do not remove this line
        """ YOUR CODE HERE """
        raise NotImplementedError
        """ YOUR CODE END HERE """
        return out
    
    def predict_proba(self, x: torch.Tensor) -> torch.Tensor:
        out = self.forward(x)
        return torch.softmax(out, dim = 1)

%%time
# do not remove – nothing to code here
# run this cell before moving on

cifar10_model, cifar10_losses = train_model(CIFARCNN(10), cifar_train_loader, epochs = 3)

# do not remove – nothing to code here
# run this cell before moving on
# but ensure get_accuracy from task 3.5 is defined

with torch.no_grad():
    cifar10_model.eval()
    for i, data in enumerate(cifar_test_loader):
        x, y = data
        x, y = x.to(device), y.to(device)
        pred = cifar10_model.predict_proba(x)
        acc = get_accuracy(pred, y)
        print(f"cifar accuracy: {acc}")
        
# don't worry if the CIFAR-10 accuracy is low, it's a tough dataset to crack.
# as long as you get something shy of 50%, you should be alright!

### Task 2.6 - Training on RGB vs. RGBA Images

import torch
import torch.nn as nn
import torch.optim as optim
import pandas as pd
import torch.nn.functional as F
from torch.utils.data import DataLoader, Dataset
from torch.nn.utils.rnn import pad_sequence

import numpy as np
import matplotlib.pyplot as plt

# Set seeds for reproducibility
torch.manual_seed(2109)
np.random.seed(2109)

# Check if GPU is available
device = "cuda" if torch.cuda.is_available() else "cpu"
print(f"Using device: {device}")

def rnn_cell_forward(xt, h_prev, Wxh, Whh, Why, bh, by):
    """
    Implements a single forward step of the RNN-cell

    Args:
        xt: 2D tensor of shape (nx, m)
            Input data at timestep "t"
        h_prev: 2D tensor of shape (nh, m)
            Hidden state at timestep "t-1"
        Wxh: 2D tensor of shape (nx, nh)
            Weight matrix multiplying the input
        Whh: 2D tensor of shape (nh, nh)
            Weight matrix multiplying the hidden state
        Why: 2D tensor of shape (nh, ny)
            Weight matrix relating the hidden-state to the output
        bh: 1D tensor of shape (nh, 1)
            Bias relating to next hidden-state
        by: 2D tensor of shape (ny, 1)
            Bias relating the hidden-state to the output

    Returns:
        yt_pred -- prediction at timestep "t", tensor of shape (ny, m)
        h_next -- next hidden state, of shape (nh, m)
    """
    # The math is extremely confusing until you realise what is actually happening:
    # 1. The memory for this timestep is some combination of:
    #    a. The memory from the previous timestep (h_prev @ Whh)
    #    b. The new information from the current input (xt @ Wxh)
    #    c. A bias term (bh)
    h_next = torch.tanh(Whh.T @ h_prev + Wxh.T @ xt + bh)

    # Whereas the output is determined by:
    #   a. The memory from the current timestep (h_next @ Why)
    #   b. A bias term (by)
    #   c. A nonlinear activation function (here, softmax)
    yt_pred = F.softmax(Why.T @ h_next + by, dim=0)

    # In other words, the RNN does not DIRECTLY use the input xt to compute the output yt_pred.
    # Instead, it will update its memory to best represent the current data,
    # and then use this memory to perform the prediction.
    
    return yt_pred, h_next


def generate_sine_wave(num_time_steps):
    """
    Generates a sine wave data

    Args:
        num_time_steps: int
            Number of time steps
    Returns:
        data: 1D tensor of shape (num_time_steps,)
            Sine wave data with corresponding time steps
    """
    x = torch.linspace(0, 16*torch.pi, num_time_steps)
    data = torch.sin(x)
    return data

num_time_steps = 1_000
sine_wave_data = generate_sine_wave(num_time_steps)

# Plot the sine wave
plt.plot(sine_wave_data)
plt.title('Sine Wave')
plt.show()

def create_sequences(sine_wave, seq_length):
    """
    Create overlapping sequences from the input time series and generate labels 
    Each label is the value immediately following the corresponding sequence.
    
    Args:
        sine_wave: A 1D tensor representing the time series data (e.g., sine wave).
        seq_length: int. The length of each sequence (window) to be used as input to the RNN.

    Returns: 
        windows: 2D tensor where each row is a sequence (window) of length `seq_length`.
        labels: 1D tensor where each element is the next value following each window.
    """
    windows = sine_wave.unfold(0, seq_length, 1)
    labels = sine_wave[seq_length:]
    return windows[:-1], labels

# Create sequences and labels
seq_length = 20
sequences, labels = create_sequences(sine_wave_data, seq_length)
# Add extra dimension to match RNN input shape [batch_size, seq_length, num_features]
sequences = sequences.unsqueeze(-1)
sequences.shape

# Split the sequences into training data (first 50%) and test data (remaining 50%) 
train_size = int(len(sequences) * 0.5)
train_seqs, train_labels = sequences[:train_size], labels[:train_size]
test_seqs, test_labels = sequences[train_size:], labels[train_size:]

### Task 3.1 - Building RNN Model

class SineRNN(nn.Module):
    def __init__(self, input_size, hidden_size, output_size):
        """
        Initialize the SineRNN model.

        Args:
            input_size (int): The number of input features per time step (typically 1 for univariate time series).
            hidden_size (int): The number of units in the RNN's hidden layer.
            output_size (int): The size of the output (usually 1 for predicting a single value).
        """
        super(SineRNN, self).__init__()
        """ YOUR CODE HERE """
        raise NotImplementedError
        """ YOUR CODE END HERE """
        
    def forward(self, x):
        """ YOUR CODE HERE """
        raise NotImplementedError
        """ YOUR CODE END HERE """

def test_task_3_1():
    input_size = output_size = 1
    hidden_size = 50
    model = SineRNN(input_size, hidden_size, output_size).to(device)
    assert [layer.detach().numpy().shape for _, layer in model.named_parameters()]\
          == [(50, 1), (50, 50), (50,), (50,), (1, 50), (1,)]

# Define loss function, and optimizer
criterion = nn.MSELoss() # Quick quiz: Why are we using MSELoss?
optimizer = torch.optim.Adam(model.parameters(), lr=5e-3)

# Training loop
num_epochs = 200
for epoch in range(num_epochs):
    model.train()
    optimizer.zero_grad()

    # Forward pass
    outputs = model(train_seqs)
    loss = criterion(outputs.squeeze(), train_labels)

    # Backward pass and optimization
    loss.backward()
    optimizer.step()
    
    if (epoch + 1) % 20 == 0:
        print(f'Epoch [{epoch+1}/{num_epochs}], Loss: {loss.item():.6f}')

# Predict on unseen data
model.eval()
y_pred = []
input_seq = test_seqs[0]  # Start with the first testing sequence

with torch.no_grad():
    for _ in range(len(test_seqs)):
        output = model(input_seq)
        y_pred.append(output.item())

        # Use the predicted value as the next input sequence
        next_seq = torch.cat((input_seq[1:, :], output.unsqueeze(0)), dim=0)
        input_seq = next_seq

# Plot the true sine wave and predictions
plt.plot(sine_wave_data, c='gray', label='Actual data')
plt.scatter(np.arange(seq_length + len(train_labels)), sine_wave_data[:seq_length + len(train_labels)], marker='.', label='Train')
x_axis_pred = np.arange(len(sine_wave_data) - len(test_labels), len(sine_wave_data))
plt.scatter(x_axis_pred, y_pred, marker='.', label='Predicted')
plt.legend(loc="lower left")
plt.show()

def create_sequences_with_noise(sine_wave, sine_wave_length, noise_length):
    """
    Create overlapping sequences from the input time series and generate labels.
    Each label is the value immediately following the corresponding sequence.
    Additionally, noise of the specified length is appended to the sequences.

    Args:
        sine_wave: A 1D tensor representing the time series data (e.g., sine wave).
        sine_wave_length: int. The length of the sine wave window.
        noise_length: int. The length of noise to be appended to each sequence.

    Returns:
        windows: 2D tensor where each row is a sequence of length `sine_wave_length + noise_length`.
        labels: 1D tensor where each element is the next value following each window.
    """
    windows = sine_wave.unfold(0, sine_wave_length, 1)
    labels = sine_wave[sine_wave_length:]
    noise = torch.randn(windows.shape[0], noise_length)
    windows = torch.cat((windows, noise), dim=1)
    return windows[:-1], labels

# Create sequences and labels
sine_wave_length = 20
noise_length = 20
sequences_noisy, labels_noisy = create_sequences_with_noise(sine_wave_data, sine_wave_length, noise_length)
# Add extra dimension to match RNN input shape [batch_size, seq_length, num_features]
sequences_noisy = sequences_noisy.unsqueeze(-1)
sequences_noisy.shape

# Split the sequences into training data (first 50%) and test data (remaining 50%) 
train_size = int(len(sequences_noisy) * 0.5)
train_seqs_noisy, train_labels = sequences_noisy[:train_size], labels_noisy[:train_size]
test_seqs_noisy, test_labels = sequences_noisy[train_size:], labels_noisy[train_size:]

# Define model
input_size = output_size = 1
hidden_size = 50
model = SineRNN(input_size, hidden_size, output_size).to(device)

# Define loss function, and optimizer
criterion = nn.MSELoss()
optimizer = torch.optim.Adam(model.parameters(), lr=5e-3)

# Training loop
num_epochs = 200
for epoch in range(num_epochs):
    model.train()
    optimizer.zero_grad()

    # Forward pass
    outputs = model(train_seqs_noisy)
    loss = criterion(outputs.squeeze(), train_labels)

    # Backward pass and optimization
    loss.backward()
    optimizer.step()

    if (epoch + 1) % 20 == 0:
        print(f'Epoch [{epoch+1}/{num_epochs}], Loss: {loss.item():.6f}')

model.eval()
with torch.no_grad():
    y_pred = model(test_seqs_noisy).squeeze()
    y_true = test_labels.squeeze()

print("Test loss:", criterion(y_pred, y_true))

plt.figure(figsize=(8, 4))
plt.plot(y_true[1::2].numpy(), label="True value", color='black')
plt.plot(y_pred[1::2].numpy(), '--', label="Predicted value", color='red')
plt.title("SineRNN Predictions")
plt.xlabel("Test sequence index")
plt.ylabel("Target value")
plt.legend()
plt.show()

### Task 4.1 - Positional Encoding Layer

class PositionalEncoding(nn.Module):
    def __init__(self):
        # You do not need to change anything in this function.
        super(PositionalEncoding, self).__init__()

    def forward(self, x: torch.Tensor) -> torch.Tensor:
        """
        Adds positional encoding to the input tensor.

        You should use vectorized operations to compute the positional encoding.
        The use of Python loops is not allowed.

        Args:
            x: Input tensor of shape (batch_size, seq_len, hidden_size)
        """
        """ YOUR CODE HERE """
        raise NotImplementedError
        """ YOUR CODE END HERE """

def test_task_4_1():
    encoder = PositionalEncoding()
    x0 = torch.zeros((1, 2, 4))
    y0 = encoder(x0)
    a0 = torch.tensor([[[0.0000, 1.0000, 0.0000, 1.0000],
                        [0.8415, 0.5403, 0.0100, 0.9999]]])
    
    assert isinstance(y0, torch.Tensor), "Output is not a tensor!"
    assert torch.allclose(y0, a0, atol=1e-4)
    
    x1 = torch.ones((1, 4, 6))
    y1 = encoder(x1)
    a1 = torch.tensor([[[1.0000, 2.0000, 1.0000, 2.0000, 1.0000, 2.0000],
                        [1.8415, 1.5403, 1.0464, 1.9989, 1.0022, 2.0000],
                        [1.9093, 0.5839, 1.0927, 1.9957, 1.0043, 2.0000],
                        [1.1411, 0.0100, 1.1388, 1.9903, 1.0065, 2.0000]]])
    
    assert torch.allclose(y1, a1, atol=1e-4)
    assert isinstance(y1, torch.Tensor), "Output is not a tensor!"

class TransformerNN(nn.Module):
    def __init__(self, input_size, hidden_size, output_size):
        """
        Initializes the TransformerNN model. We use the same hidden size for the feedforward network and the Transformer encoder.

        Args:
            input_size (int): The number of input features per time step (typically 1 for univariate time series).
            hidden_size (int): The number of units in the Transformer's hidden layers.
            output_size (int): The size of the output (usually 1 for predicting a single value).
        """
        super(TransformerNN, self).__init__()
        self.embedding = nn.Linear(input_size, hidden_size)
        self.positional_encoder = PositionalEncoding()
        encoder_layer = nn.TransformerEncoderLayer(d_model=hidden_size, dim_feedforward=hidden_size, nhead=1, batch_first=True)
        self.transformer_encoder = nn.TransformerEncoder(encoder_layer, num_layers=1)
        self.fc_out = nn.Linear(hidden_size, output_size)

    def forward(self, x):
        x = self.embedding(x)
        x = self.positional_encoder(x)
        x = self.transformer_encoder(x)

        # The encoder outputs a sequence of hidden states, so
        # we take the mean across the sequence length dimension.
        x = x.mean(dim=1)

        out = self.fc_out(x)
        return out

model = TransformerNN(input_size=1, hidden_size=50, output_size=1)
criterion = nn.MSELoss()
optimizer = torch.optim.Adam(model.parameters(), lr=0.001)

num_epochs = 200
for epoch in range(num_epochs):
    model.train()
    optimizer.zero_grad()
    outputs = model(train_seqs_noisy)
    loss = criterion(outputs.squeeze(), train_labels)
    loss.backward()
    optimizer.step()

    if (epoch + 1) % 20 == 0:
        print(f'Epoch [{epoch+1}/{num_epochs}], Loss: {loss.item():.6f}')


model.eval()
with torch.no_grad():
    y_pred = model(test_seqs).squeeze()
    y_true = test_labels.squeeze()

print("Test loss:", criterion(y_pred, y_true))

plt.figure(figsize=(8, 4))
plt.plot(y_true[1::2].numpy(), label="True value", color='black')
plt.plot(y_pred[1::2].numpy(), '--', label="Predicted value", color='red')
plt.title("TransformerNN Predictions")
plt.xlabel("Test sequence index")
plt.ylabel("Target value")
plt.legend()
plt.show()

### Task 4.2 - Visualizing Attention Scores

### Task 4.3 - Discovering the Ingredients to Transformers' Success

# You may use this cell and create new cells to experiment.


if __name__ == '__main__':
    test_task_1_1()
    test_task_1_2()
    test_task_1_3()
    test_task_2_1()
    test_task_2_3()
    test_task_2_4()
    test_task_3_1()
    test_task_4_1()