#!/usr/bin/env python
# coding: utf-8

# # Gesture Recognition
#  - Developers: Sreedhar K and Munirathinam Duraisamy
# 
#  Note:
#  For the submitted assignment (with the .h5 file), we can download from the following link:
#  https://mail.google.com/mail/u/0/?tab=rm&ogbl#search/model5/FMfcgzGrbvFHSjwJZWpRhrkJkpxqwDvb

# # Table of contents:
# 
# - [Introduction](#Introduction)
# - [Problem Statement](#Problem_Statement)
# - [Generator](#Generator)
# - [Models](#Model)
#     - Conv3D:
#     -- [Model 1: No of Epochs = 15 , batch_size = 64 ,shape = (120,120) , no of frames = 10](#Model_1)
#     -- [Model 2: No of Epochs = 20 , batch_size = 20 ,shape = (50,50) , no of frames = 6](#Model_2)
#     -- [Model 3: No of Epochs = 20 , batch_size = 30 ,shape = (50,50) , no of frames = 10](#Model_3)
#     -- [Model 4: No of Epochs = 25 , batch_size = 50 ,shape = (120,120) , no of frames = 10](#Model_4)
#     -- [Model 5: No of Epochs = 25 , batch_size = 50 ,shape = (70,70) , no of frames = 18](#Model_5)
#     - CNN + RNN : CNN2D LSTM Model - TimeDistributed
#     -- [Model 6: No of Epochs = 25 , batch_size = 50 ,shape = (70,70), no of frames = 18](#Model_6)
#     -- [Model 7: No of Epochs = 20 , number of batches=20 ,shape = (50,50), number of frames=10](#Model_7)
#     - CONV2D + GRU
#     -- [Model 8: No of frames are 18 , image_height and image_witdth = (50,50) , batch_size 20 , no of epochs = 20](#Model_8)
#     - Transfer Learning Using MobileNet
#     -- [Model 9:  No of epochs = 15; batch_size = 5; shape (120,120); no of frames = 18](#Model_9)
# - [Conclusion](#Conclusion)

# <h2><a id="Introduction">Introduction</a></h2>
# 
# In this group project, we are going to build a different model that will be able to predict the 5 gestures correctly.

# <h2><a id="Problem_Statement">Problem Statement</a></h2>
# 
#     - We want to develop a cool feature in the smart-TV that can recognise five different gestures performed by the user which will help users control the TV without using a remote.
#     - The gestures are continuously monitored by the webcam mounted on the TV. Each gesture corresponds to a specific command:
#         -- Thumbs up:  Increase the volume
#         -- Thumbs down: Decrease the volume
#         -- Left swipe: 'Jump' backwards 10 seconds
#         -- Right swipe: 'Jump' forward 10 seconds  
#         -- Stop: Pause the movie

# In[ ]:


get_ipython().system('pip install numpy pandas imageio kagglehub tensorflow keras scikit-image')


# In[1]:


# Import the following libraries to get started.
import numpy as np
import os
#from scipy.misc import imread, imresize
import imageio
from PIL import Image
import datetime


# In[2]:


import kagglehub

# Download latest version
path = kagglehub.dataset_download("imsparsh/gesture-recognition")

print("Path to dataset files:", path)


# We set the random seed so that the results don't vary drastically.

# In[3]:


np.random.seed(30)
import random as rn
rn.seed(30)
from keras import backend as K
import tensorflow as tf
tf.random.set_seed(30)


# In[4]:


get_ipython().system('ls /home/runner/.cache/kagglehub/datasets/imsparsh/gesture-recognition/versions/2')
get_ipython().system('cd')

# For Google Colab
# !chmod 777 /root/.cache/kagglehub/datasets/imsparsh/gesture-recognition/versions/2/train.csv
# !chmod 777 /root/.cache/kagglehub/datasets/imsparsh/gesture-recognition/versions/2/val.csv

# For github workflow
get_ipython().system('chmod 777 /home/runner/.cache/kagglehub/datasets/imsparsh/gesture-recognition/versions/2/train.csv')
get_ipython().system('chmod 777 /home/runner/.cache/kagglehub/datasets/imsparsh/gesture-recognition/versions/2/val.csv')


# In this block, you read the folder names for training and validation. You also set the `batch_size` here. Note that you set the batch size in such a way that you are able to use the GPU in full capacity. You keep increasing the batch size until the machine throws an error.

# In[5]:


# Google Colab
# train_doc = np.random.permutation(open('/root/.cache/kagglehub/datasets/imsparsh/gesture-recognition/versions/2/train.csv').readlines())
# val_doc = np.random.permutation(open('/root/.cache/kagglehub/datasets/imsparsh/gesture-recognition/versions/2/val.csv').readlines())

# Github workflow
train_doc = np.random.permutation(open('/home/runner/.cache/kagglehub/datasets/imsparsh/gesture-recognition/versions/2/train.csv').readlines())
val_doc = np.random.permutation(open('/home/runner/.cache/kagglehub/datasets/imsparsh/gesture-recognition/versions/2/val.csv').readlines())


# <h2><a id="Generator">Generator</a></h2>
# 
# This is one of the most important parts of the code. In the generator, we are going to pre-process the images as we have images of different dimensions (50 x 50, 70 x 70 and 120 x 120) as well as create a batch of video frames. The generator should be able to take a batch of videos as input without any error. Steps like cropping/resizing and normalization should be performed successfully.  We have to experiment with `img_idx`, `y`,`z` and normalization such that we get high accuracy.

# In[6]:


from PIL import Image
#!pip install scikit-image
from skimage.transform import resize


# In[7]:


def generator(source_path, folder_list, batch_size):
    print( 'Source path = ', source_path, '; batch size =', batch_size)
    #img_idx = #create a list of image numbers you want to use for a particular video
    while True:
        #Shuffle the list of the folders in csv
        t = np.random.permutation(folder_list)
         #Exact batches of the batch size
        num_batches = int(len(t)/batch_size)
         #Left over batches which should be handled separately
        leftover_batches = len(t) - num_batches * batch_size

        for batch in range(num_batches): # we iterate over the number of batches
            batch_data = np.zeros((batch_size,len(img_idx),shape_h, shape_w,3)) # x is the number of images you use for each video, (y,z) is the final size of the input images and 3 is the number of channels RGB
            batch_labels = np.zeros((batch_size,5)) # batch_labels is the one hot representation of the output
            for folder in range(batch_size): # iterate over the batch_size
                imgs = os.listdir(source_path+'/'+ t[folder + (batch*batch_size)].split(';')[0]) # read all the images in the folder
                for idx,item in enumerate(img_idx): #  Iterate iver the frames/images of a folder to read them in
                    image = imageio.imread(source_path+'/'+ t[folder + (batch*batch_size)].strip().split(';')[0]+'/'+imgs[item]).astype(np.float32)

                    #crop the images and resize them. Note that the images are of 2 different shape
                    #and the conv3D will throw error if the inputs in a batch have different shapes

                    image = resize(image, (shape_h,shape_w))
                    batch_data[folder,idx,:,:,0] = (image[:,:,0]) - 104
                    batch_data[folder,idx,:,:,1] = (image[:,:,1]) - 117
                    batch_data[folder,idx,:,:,2] = (image[:,:,2]) - 123

                #Fill the one hot encoding stuff where we maintain the label
                batch_labels[folder, int(t[folder + (batch*batch_size)].strip().split(';')[2])] = 1
            yield batch_data, batch_labels #you yield the batch_data and the batch_labels, remember what does yield do


        # write the code for the remaining data points which are left after full batches
        if leftover_batches != 0:
            for batch in range(num_batches):
                # x is the number of images you use for each video, (y,z) is the final size of the input images and 3 is the number of channels RGB
                batch_data = np.zeros((batch_size,len(img_idx),shape_h, shape_w,3))
                # batch_labels is the one hot representation of the output: 10 videos with 5 columns as classes
                batch_labels = np.zeros((batch_size,5))
                for folder in range(batch_size): # iterate over the batch_size
                    imgs = os.listdir(source_path +'/'+t[batch * batch_size + folder].split(';')[0])
                    for idx,item in enumerate(img_idx): #  Iterate iver the frames/images of a folder to read them in

                        image = imageio.imread(source_path +'/'+t[batch * batch_size + folder].split(';')[0] +'/'+imgs[item]).astype(np.float32)
                        image = resize(image, (shape_h,shape_w))

                        batch_data[folder,idx,:,:,0] = (image[:,:,0]) - 104
                        batch_data[folder,idx,:,:,1] = (image[:,:,1]) - 117
                        batch_data[folder,idx,:,:,2] = (image[:,:,2]) - 123

                    #Fill the one hot encoding stuff where we maintain the label
                    batch_labels[folder, int(t[batch * batch_size + folder].split(';')[2])] = 1
                yield batch_data, batch_labels #you yield the batch_data and the batch_labels, remember what does yield do



# A video is represented above in the generator as (number of images, height, width, number of channels). We take this into consideration while creating the model architecture.

# In[8]:


curr_dt_time = datetime.datetime.now()

#Google colab
# train_path = '/root/.cache/kagglehub/datasets/imsparsh/gesture-recognition/versions/2/train'
# val_path = '/root/.cache/kagglehub/datasets/imsparsh/gesture-recognition/versions/2/val'

# Github workflow
train_path = '/home/runner/.cache/kagglehub/datasets/imsparsh/gesture-recognition/versions/2/train'
val_path = '/home/runner/.cache/kagglehub/datasets/imsparsh/gesture-recognition/versions/2/val'

num_train_sequences = len(train_doc)
print('# training sequences =', num_train_sequences)
num_val_sequences = len(val_doc)
print('# validation sequences =', num_val_sequences)


# <h2><a id="Model">Model</a></h2>
# 
# Here we make the model using different functionalities that Keras provides. We must use `Conv3D` and `MaxPooling3D` and not `Conv2D` and `Maxpooling2D` for a 3D convolution model. We would also use `TimeDistributed` while building a Conv2D + RNN model. Also, the last layer is the softmax. We design the network in such a way that the model is able to give good accuracy on the least number of parameters so that it can fit in the memory of the webcam.

# In[9]:


from keras.models import Sequential, Model
from keras.layers import Dense, GRU, Flatten, TimeDistributed, Flatten, BatchNormalization, Activation,  Dropout, LSTM, ConvLSTM2D
from tensorflow.keras import regularizers
from keras.layers import Conv3D, MaxPooling3D
from keras.callbacks import ModelCheckpoint, ReduceLROnPlateau, EarlyStopping
from keras import optimizers


#write your model here
class Conv3DModel():

    def Model3D(self,frames_to_sample,image_height,image_width):

        model = Sequential()
        model.add(Conv3D(64, (3,3,3), strides=(1,1,1), padding='same', input_shape=(frames_to_sample,image_height,image_width,3)))
        model.add(BatchNormalization())
        model.add(Activation('elu'))
        model.add(MaxPooling3D(pool_size=(2,2,1), strides=(2,2,1)))

        model.add(Conv3D(128, (3,3,3), strides=(1,1,1), padding='same'))
        model.add(BatchNormalization())
        model.add(Activation('elu'))
        model.add(MaxPooling3D(pool_size=(2,2,2), strides=(2,2,2), padding='same'))

        # model.add(Dropout(0.25))

        model.add(Conv3D(256, (3,3,3), strides=(1,1,1), padding='same'))
        model.add(BatchNormalization())
        model.add(Activation('elu'))
        model.add(MaxPooling3D(pool_size=(2,2,2), strides=(2,2,2), padding='same'))

        # model.add(Dropout(0.25))

        model.add(Conv3D(256, (3,3,3), strides=(1,1,1), padding='same'))
        model.add(BatchNormalization())
        model.add(Activation('elu'))
        model.add(MaxPooling3D(pool_size=(2,2,2), strides=(2,2,2), padding='same'))

        model.add(Flatten())

        model.add(Dropout(0.5))
        model.add(Dense(512, activation='elu'))
        model.add(Dropout(0.5))
        model.add(Dense(5, activation='softmax'))

        #write your optimizer TRY OUT WITH ADAM AND SGD
        '''
        Classes
        class Adadelta: Optimizer that implements the Adadelta algorithm.

        class Adagrad: Optimizer that implements the Adagrad algorithm.

        class Adam: Optimizer that implements the Adam algorithm.

        class Adamax: Optimizer that implements the Adamax algorithm.

        class Ftrl: Optimizer that implements the FTRL algorithm.

        class Nadam: Optimizer that implements the NAdam algorithm.

        class Optimizer: Base class for Keras optimizers.

        class RMSprop: Optimizer that implements the RMSprop algorithm.

        class SGD: Gradient descent (with momentum) optimizer.
        '''

        optimiser = tf.keras.optimizers.SGD(learning_rate=0.001, decay=1e-6, momentum=0.7, nesterov=True)
        model.compile(optimizer=optimiser, loss='categorical_crossentropy', metrics=['categorical_accuracy'])
        return model


# Once we have written the model, the next step is to `compile` the model. When we print the `summary` of the model, we can see the total number of parameters we have to train.

# In[10]:


#Global vars
def global_vars(img_idx,shape_h,shape_w,batch_size,num_epochs):
    print("the number of images we will be feeding in the input for a video {}".format(len(img_idx)))
    return img_idx,shape_h,shape_w,batch_size,num_epochs


# <h2><a id="Model_1">Model 1:</a></h2>

# In[11]:


# Model 1: No of Epochs = 15 , batch_size = 64 ,shape = (120,120) , no of frames = 10

img_idx,shape_h,shape_w,batch_size,num_epochs = global_vars([6,8,10,12,14,16,20,22,24,26],120,120,64,15)
conv_model1=Conv3DModel()
conv_model1=conv_model1.Model3D(frames_to_sample=len(img_idx),image_height=shape_h,image_width=shape_w)
print(conv_model1.summary())


# Let us create the `train_generator` and the `val_generator` which will be used in `.fit_generator`.

# In[12]:


train_generator = generator(train_path, train_doc, batch_size)
val_generator = generator(val_path, val_doc, batch_size)


# In[13]:


model_name = 'model_init' + '_' + str(curr_dt_time).replace(' ','').replace(':','_') + '/'

if not os.path.exists(model_name):
    os.mkdir(model_name)

#Fix the file path
filepath = model_name + 'model-{epoch:05d}-{loss:.5f}-{categorical_accuracy:.5f}-{val_loss:.5f}-{val_categorical_accuracy:.5f}.h5'

#Callback to save the Keras model or model weights at some frequency.
#ModelCheckpoint callback is used in conjunction with training using model.fit() to save a model or weights.
#path to save the model file.
#"val_loss" to monitor the model's total loss in validation.
#saves when the model is considered the "best"
#the model's weights will be saved
#the minimization of the monitored quantity
checkpoint = ModelCheckpoint(filepath, monitor='val_loss', verbose=1, save_best_only=True, save_weights_only=False, mode='auto',save_freq='epoch')

#Reduce learning rate when a metric has stopped improving.
#LR = ReduceLROnPlateau(monitor, factor, aptience, min_lr)
#monitor: quantity to be monitored.
#factor: factor by which the learning rate will be reduced. new_lr = lr * factor.
#patience: number of epochs with no improvement after which learning rate will be reduced.
#min_lr: lower bound on the learning rate.
LR = ReduceLROnPlateau(monitor='val_loss', factor=0.5, patience=2, verbose=1, mode='min', epsilon=0.0001, cooldown=0, min_lr=0.00001)

EarlyStop = EarlyStopping(monitor='val_loss', patience=6 )
# write the REducelronplateau code here
callbacks_list = [checkpoint, LR]


# The `steps_per_epoch` and `validation_steps` are used by `fit_generator` to decide the number of next() calls it need to make.

# In[14]:


if (num_train_sequences%batch_size) == 0:
    steps_per_epoch = int(num_train_sequences/batch_size)
else:
    steps_per_epoch = (num_train_sequences//batch_size) + 1

if (num_val_sequences%batch_size) == 0:
    validation_steps = int(num_val_sequences/batch_size)
else:
    validation_steps = (num_val_sequences//batch_size) + 1


# In[15]:


print(steps_per_epoch)
print(validation_steps)


# Let us now fit the model. This will start training the model and with the help of the checkpoints, you'll be able to save the model at the end of each epoch.

# In[ ]:


# conv_model1.fit(train_generator, steps_per_epoch=steps_per_epoch, epochs=num_epochs, verbose=1,
#                      callbacks=callbacks_list, validation_data=val_generator,
#                      validation_steps=validation_steps, class_weight=None, initial_epoch=0)


# #### Insights:
#     Model 1 is giving the out of memory error with batch size 64. We try with less batch size and shapes to further improve the performance

# <h2><a id="Model_2">Model 2:</a></h2>

# In[ ]:


# Model 2: No of Epochs = 20; batch_size = 20; shape = (50,50); no of frames = 6

img_idx,shape_h,shape_w,batch_size,num_epochs = global_vars(list(range(0,30,5)),50,50,20,20)
conv_model2=Conv3DModel()
conv_model2=conv_model2.Model3D(frames_to_sample=len(img_idx),image_height=shape_h,image_width=shape_w)
print(conv_model2.summary())


# In[ ]:


train_generator = generator(train_path, train_doc, batch_size)
val_generator = generator(val_path, val_doc, batch_size)

if (num_train_sequences%batch_size) == 0:
    steps_per_epoch = int(num_train_sequences/batch_size)
else:
    steps_per_epoch = (num_train_sequences//batch_size) + 1

if (num_val_sequences%batch_size) == 0:
    validation_steps = int(num_val_sequences/batch_size)
else:
    validation_steps = (num_val_sequences//batch_size) + 1

print(steps_per_epoch)
print(validation_steps)


# In[ ]:


conv_model2.fit(train_generator, steps_per_epoch=steps_per_epoch, epochs=num_epochs, verbose=1,
                    callbacks=callbacks_list, validation_data=val_generator,
                    validation_steps=validation_steps, class_weight=None, initial_epoch=0)


# #### Insights:
#     - Number of Epochs =20; Batch size=20; Number of frames=6
#     - Taking the Frames with the step size 5 and taking 6 frames with shape (50,50) have increased the performance tremendously for both the training and validation set

# <h2><a id="Model_3">Model 3: </a></h2>

# In[ ]:


# #No of Epochs = 20; batch_size = 30; shape = (50,50); no of frames = 10
# img_idx,shape_h,shape_w,batch_size,num_epochs = global_vars(list(range(0,30,3)),50,50,20,20)
# conv_model3=Conv3DModel()
# conv_model3=conv_model3.Model3D(frames_to_sample=len(img_idx),image_height=shape_h,image_width=shape_w)
# conv_model3.summary()


# # In[ ]:


# train_generator = generator(train_path, train_doc, batch_size)
# val_generator = generator(val_path, val_doc, batch_size)

# if (num_train_sequences%batch_size) == 0:
#     steps_per_epoch = int(num_train_sequences/batch_size)
# else:
#     steps_per_epoch = (num_train_sequences//batch_size) + 1

# if (num_val_sequences%batch_size) == 0:
#     validation_steps = int(num_val_sequences/batch_size)
# else:
#     validation_steps = (num_val_sequences//batch_size) + 1

# print(steps_per_epoch)
# print(validation_steps)


# # In[ ]:


# conv_model3.fit(train_generator, steps_per_epoch=steps_per_epoch, epochs=num_epochs, verbose=1,
#                     callbacks=callbacks_list, validation_data=val_generator,
#                     validation_steps=validation_steps, class_weight=None, initial_epoch=0)


# # #### Insights:
# #     Model 3: Number of Epochs =20; Batch size=30; shape = (50,50); Number of frames=10
# #     Keeping the same shape and increasing the number of frames we have observed that Validation Accuracy decreased and slightly seems to be overfitting as compared to Model-2
# #     
# #     

# # <h2><a id="Model_4">Model 4: </a></h2>

# # In[ ]:


# #No of Epochs = 25 , batch_size = 50 ,shape = (100,100) , no of frames = 10
# img_idx,shape_h,shape_w,batch_size,num_epochs = global_vars(list(range(5,28,2)),100,100,50,25)
# conv_model4=Conv3DModel()
# conv_model4=conv_model4.Model3D(frames_to_sample=len(img_idx),image_height=shape_h,image_width=shape_w)
# conv_model4.summary()


# # In[ ]:


# train_generator = generator(train_path, train_doc, batch_size)
# val_generator = generator(val_path, val_doc, batch_size)

# if (num_train_sequences%batch_size) == 0:
#     steps_per_epoch = int(num_train_sequences/batch_size)
# else:
#     steps_per_epoch = (num_train_sequences//batch_size) + 1

# if (num_val_sequences%batch_size) == 0:
#     validation_steps = int(num_val_sequences/batch_size)
# else:
#     validation_steps = (num_val_sequences//batch_size) + 1

# print(steps_per_epoch)
# print(validation_steps)


# # In[ ]:


# conv_model4.fit(train_generator, steps_per_epoch=steps_per_epoch, epochs=num_epochs, verbose=1,
#                      callbacks=callbacks_list, validation_data=val_generator,
#                      validation_steps=validation_steps, class_weight=None, initial_epoch=0)


# # #### Insights:
# # Model 4: This model seems to be overfitting. Increasing the image size decreases the accuracy.

# # <h2><a id="Model_5">Model 5: </a></h2>

# # In[ ]:


# #No of Epochs = 25 , batch_size = 50 ,shape = (70,70) , no of frames = 18
# img_idx,shape_h,shape_w,batch_size,num_epochs = global_vars([0,1,2,4,6,8,10,12,14,16,18,20,22,24,26,27,28,29],70,70,50,34)
# conv_model5=Conv3DModel()
# conv_model5=conv_model5.Model3D(frames_to_sample=len(img_idx),image_height=shape_h,image_width=shape_w)
# conv_model5.summary()

# train_generator = generator(train_path, train_doc, batch_size)
# val_generator = generator(val_path, val_doc, batch_size)


# # In[ ]:


# if (num_train_sequences%batch_size) == 0:
#     steps_per_epoch = int(num_train_sequences/batch_size)
# else:
#     steps_per_epoch = (num_train_sequences//batch_size) + 1

# if (num_val_sequences%batch_size) == 0:
#     validation_steps = int(num_val_sequences/batch_size)
# else:
#     validation_steps = (num_val_sequences//batch_size) + 1


# # In[ ]:


# print(steps_per_epoch)
# print(validation_steps)


# # In[ ]:


# conv_model5.fit(train_generator, steps_per_epoch=steps_per_epoch, epochs=num_epochs, verbose=1,
#                     callbacks=callbacks_list, validation_data=val_generator,
#                     validation_steps=validation_steps, class_weight=None, initial_epoch=0)


# # #### Insights:
# #     Model 5 is clearly an overfit model can see that increasing in number of frames and epochs causing the noise to be learned also from all the frames

# # #### Overall Insights for Model 1 to 5:
# #     Based on our experiment the final model will be model 2 - Less no of frames and reducing image size to 50,50 giving good results
# #     Model 2 No of Epochs = 20 , batch_size = 20 ,shape = (50,50) , no of frames = 6

# # <h2><a id="Model_6">Model 6 <br></a></h2>

# # In[ ]:


# #Taking image_height and image_width as 70,70 , batch size 50 and no of epochs 25
# #Switching Model architecture to Conv2D+LSTM
# # Conv2D_18, 70, 70, 16
# # LSTM_512
# # Dense_512_5

# from keras.layers.convolutional import  Conv2D, MaxPooling2D
# from keras.layers import TimeDistributed,LSTM ,ConvLSTM2D
# model = Sequential([
#     TimeDistributed(Conv2D(16, (3,3), padding='same', activation='relu'), input_shape=(len(img_idx),shape_h,shape_w,3)),
#     TimeDistributed(BatchNormalization()),
#     TimeDistributed(MaxPooling2D((2,2))),

#     TimeDistributed(Conv2D(32, (3,3), padding='same', activation='relu')),
#     TimeDistributed(BatchNormalization()),
#     TimeDistributed(MaxPooling2D((2,2))),

#     TimeDistributed(Conv2D(64, (3,3), padding='same', activation='relu')),
#     TimeDistributed(BatchNormalization()),
#     TimeDistributed(MaxPooling2D((2,2))),

#     TimeDistributed(Conv2D(128, (3,3), padding='same', activation='relu')),
#     TimeDistributed(BatchNormalization()),
#     TimeDistributed(MaxPooling2D((2,2))),

#     TimeDistributed(Conv2D(256, (3,3), padding='same', activation='relu')),
#     TimeDistributed(BatchNormalization()),
#     TimeDistributed(MaxPooling2D((2,2))),

#     TimeDistributed(Flatten()),
#     LSTM(512),
#     Dropout(0.2),

#     Dense(512, activation='relu'),
#     Dropout(0.2),

#     Dense(5, activation='softmax')
# ], name="conv_2d_lstm")


# # In[ ]:


# optimiser = tf.keras.optimizers.SGD(lr=0.001, decay=1e-6, momentum=0.7, nesterov=True)
# model.compile(optimizer=optimiser, loss='categorical_crossentropy', metrics=['categorical_accuracy'])


# # In[ ]:


# print(model.summary())


# # In[ ]:


# train_generator = generator(train_path, train_doc, 20)
# val_generator = generator(val_path, val_doc, 20)


# # In[ ]:


# if (num_train_sequences%batch_size) == 0:
#     steps_per_epoch = int(num_train_sequences/batch_size)
# else:
#     steps_per_epoch = (num_train_sequences//batch_size) + 1

# if (num_val_sequences%batch_size) == 0:
#     validation_steps = int(num_val_sequences/batch_size)
# else:
#     validation_steps = (num_val_sequences//batch_size) + 1


# # In[ ]:


# print(steps_per_epoch)
# print(validation_steps)


# # In[ ]:


# model.fit(train_generator, steps_per_epoch=steps_per_epoch, epochs=num_epochs, verbose=1,
#                     callbacks=callbacks_list, validation_data=val_generator,
#                     validation_steps=validation_steps, class_weight=None, initial_epoch=0)


# # #### Insights:
# #     Model-6 is clearly overfitting.
# #     We will change the number of frames, image size and check

# # <h2><a id="Model_7">Model 7:</a></h2>

# #     No of Epochs = 20 , number of batches=20 ,shape = (50,50), number of frames=10
# #     img_idx,shape_h,shape_w,batch_size,num_epochs = global_vars(list(range(0,30,3)),50,50,20,20)

# # The number of images we will be feeding in the input for a video 10

# # In[ ]:


# #Switching Model architecture to Conv2D+LSTM

# from keras.layers.convolutional import  Conv2D, MaxPooling2D
# from keras.layers import TimeDistributed,LSTM ,ConvLSTM2D
# model = Sequential([
#     TimeDistributed(Conv2D(16, (3,3), padding='same', activation='relu'), input_shape=(len(img_idx),shape_h,shape_w,3)),
#     TimeDistributed(BatchNormalization()),
#     TimeDistributed(MaxPooling2D((2,2))),

#     TimeDistributed(Conv2D(32, (3,3), padding='same', activation='relu')),
#     TimeDistributed(BatchNormalization()),
#     TimeDistributed(MaxPooling2D((2,2))),

#     TimeDistributed(Conv2D(64, (3,3), padding='same', activation='relu')),
#     TimeDistributed(BatchNormalization()),
#     TimeDistributed(MaxPooling2D((2,2))),

#     TimeDistributed(Conv2D(128, (3,3), padding='same', activation='relu')),
#     TimeDistributed(BatchNormalization()),
#     TimeDistributed(MaxPooling2D((2,2))),

#     TimeDistributed(Conv2D(256, (3,3), padding='same', activation='relu')),
#     TimeDistributed(BatchNormalization()),
#     TimeDistributed(MaxPooling2D((2,2))),

#     TimeDistributed(Flatten()),
#     LSTM(512),
#     Dropout(0.2),

#     Dense(512, activation='relu'),
#     Dropout(0.2),

#     Dense(5, activation='softmax')
# ], name="conv_2d_lstm")


# # In[ ]:


# optimiser = tf.keras.optimizers.SGD(lr=0.001, decay=1e-6, momentum=0.7, nesterov=True)
# model.compile(optimizer=optimiser, loss='categorical_crossentropy', metrics=['categorical_accuracy'])


# # In[ ]:


# print(model.summary())


# # In[ ]:


# train_generator = generator(train_path, train_doc, batch_size)
# val_generator = generator(val_path, val_doc, batch_size)


# # In[ ]:


# if (num_train_sequences%batch_size) == 0:
#     steps_per_epoch = int(num_train_sequences/batch_size)
# else:
#     steps_per_epoch = (num_train_sequences//batch_size) + 1

# if (num_val_sequences%batch_size) == 0:
#     validation_steps = int(num_val_sequences/batch_size)
# else:
#     validation_steps = (num_val_sequences//batch_size) + 1


# # In[ ]:


# print(steps_per_epoch)
# print(validation_steps)


# # In[ ]:


# model.fit(train_generator, steps_per_epoch=steps_per_epoch, epochs=num_epochs,verbose=1,
#                     callbacks=callbacks_list, validation_data=val_generator,
#                     validation_steps=validation_steps, class_weight=None, initial_epoch=0)


# # #### Insights:
# #     Model 7 is also clearly overfitting

# # <h2><a id="Model_8">Model 8: </a></h2>

# # CONV2D + GRU Changed the no of layers , no of frames are 18 , image_height and image_witdth = (50,50) , batch_size 20 , no of epochs = 20

# # In[ ]:


# img_idx,shape_h,shape_w,batch_size,num_epochs = global_vars([0,1,2,4,6,8,10,12,14,16,18,20,22,24,26,27,28,29],50,50,20,20)


# # In[ ]:


# from keras.layers.convolutional import  Conv2D, MaxPooling2D
# from keras.layers import TimeDistributed,LSTM ,ConvLSTM2D
# model = Sequential()
# model.add(TimeDistributed(Conv2D(16, (3, 3) , padding='same', activation='relu'),
#                                   input_shape=(len(img_idx),shape_h,shape_w,3)))
# model.add(TimeDistributed(BatchNormalization()))
# model.add(TimeDistributed(MaxPooling2D((2, 2))))

# model.add(TimeDistributed(Conv2D(32, (3, 3) , padding='same', activation='relu')))
# model.add(TimeDistributed(BatchNormalization()))
# model.add(TimeDistributed(MaxPooling2D((2, 2))))

# model.add(TimeDistributed(Conv2D(64, (3, 3) , padding='same', activation='relu')))
# model.add(TimeDistributed(BatchNormalization()))
# model.add(TimeDistributed(MaxPooling2D((2, 2))))

# model.add(TimeDistributed(Conv2D(128, (3, 3) , padding='same', activation='relu')))
# model.add(TimeDistributed(BatchNormalization()))
# model.add(TimeDistributed(MaxPooling2D((2, 2))))


# model.add(TimeDistributed(Flatten()))


# model.add(GRU(64))
# model.add(Dropout(0.25))

# model.add(Dense(64,activation='relu'))
# model.add(Dropout(0.25))

# model.add(Dense(5, activation='softmax'))


# # In[ ]:


# optimiser = tf.keras.optimizers.SGD(lr=0.001, decay=1e-6, momentum=0.7, nesterov=True)
# model.compile(optimizer=optimiser, loss='categorical_crossentropy', metrics=['categorical_accuracy'])


# # In[ ]:


# print(model.summary())


# # In[ ]:


# train_generator = generator(train_path, train_doc, batch_size)
# val_generator = generator(val_path, val_doc, batch_size)


# # In[ ]:


# if (num_train_sequences%batch_size) == 0:
#     steps_per_epoch = int(num_train_sequences/batch_size)
# else:
#     steps_per_epoch = (num_train_sequences//batch_size) + 1

# if (num_val_sequences%batch_size) == 0:
#     validation_steps = int(num_val_sequences/batch_size)
# else:
#     validation_steps = (num_val_sequences//batch_size) + 1


# # In[ ]:


# print(steps_per_epoch)
# print(validation_steps)


# # In[ ]:


# model.fit(train_generator, steps_per_epoch=steps_per_epoch, epochs=num_epochs,verbose=1,
#                     callbacks=callbacks_list, validation_data=val_generator,
#                     validation_steps=validation_steps, class_weight=None, initial_epoch=0)


# # #### Insights:
# #     Model 8 is overfitting

# # <h2><a id="Model_9">Model 9 Using Transfer Learning - MobileNet</a></h2>

# # In[ ]:


# img_idx,shape_h,shape_w,batch_size,num_epochs = global_vars([0,1,2,4,6,8,10,12,14,16,18,20,22,24,26,27,28,29],120,120,5,15)


# # In[ ]:


# from keras.layers.convolutional import  Conv2D, MaxPooling2D
# from keras.layers import TimeDistributed,LSTM ,ConvLSTM2D
# from keras.applications import mobilenet
# mobilenet_transfer = mobilenet.MobileNet(weights='imagenet', include_top=False)

# model = Sequential()
# model.add(TimeDistributed(mobilenet_transfer,input_shape=(len(img_idx),shape_h,shape_w,3)))

# model.add(TimeDistributed(BatchNormalization()))
# model.add(TimeDistributed(MaxPooling2D((2, 2))))
# model.add(TimeDistributed(Flatten()))


# model.add(GRU(128))
# model.add(Dropout(0.25))

# model.add(Dense(128,activation='relu'))
# model.add(Dropout(0.25))

# model.add(Dense(5, activation='softmax'))


# # In[ ]:


# optimiser = tf.keras.optimizers.Adam()
# model.compile(optimizer=optimiser, loss='categorical_crossentropy', metrics=['categorical_accuracy'])


# # In[ ]:


# print(model.summary())


# # In[ ]:


# train_generator = generator(train_path, train_doc, batch_size)
# val_generator = generator(val_path, val_doc, batch_size)


# # In[ ]:


# if (num_train_sequences%batch_size) == 0:
#     steps_per_epoch = int(num_train_sequences/batch_size)
# else:
#     steps_per_epoch = (num_train_sequences//batch_size) + 1

# if (num_val_sequences%batch_size) == 0:
#     validation_steps = int(num_val_sequences/batch_size)
# else:
#     validation_steps = (num_val_sequences//batch_size) + 1


# # In[ ]:


# print(steps_per_epoch)
# print(validation_steps)


# # In[ ]:


# model.fit(train_generator, steps_per_epoch=steps_per_epoch, epochs=num_epochs,verbose=1,
#                     callbacks=callbacks_list, validation_data=val_generator,
#                     validation_steps=validation_steps, class_weight=None, initial_epoch=0)


# # <h2><a id="Conclusion">Conclusion</a></h2>
# # 
# # - # Model Statistics
# # 
# # - # Conv3D
# # 
# # - Model 1 : No of Epochs = 15 , batch_size = 64 ,shape = (120,120) , no of frames = 10
# # - - - - Model 1 is giving the out of memory error with batch size 64. We try with less batch size and shapes to further improve the performance and accuracy
# # 
# # - Model 2 : No of Epochs = 20 , batch_size = 20 ,shape = (50,50) , no of frames = 6
# # 
# # - - - - Training Accuracy : 95.74% , Validation Accuracy : 89% ,
# # - - - - Model Analysis : Training and validation Accuracy are good so that we can conclude that with above set of parameters model is giving good results
# # 
# # - Model 3 : No of Epochs = 20 , batch_size = 30 ,shape = (50,50) , no of frames = 10
# # 
# # - - - - Training Accuracy : 95.29% , Validation Accuracy : 87%
# # - - - - Model Analysis : Keeping the same shape and increasing the number of frames we have observed that validation accuracy decreased and seems to be overfitting as compared to Model-2
# # 
# # - Model 4 : No of Epochs = 25 , batch_size = 50 ,shape = (100,100) , no of frames = 10
# # 
# # - - - - Training Accuracy : 91.71% , Validation Accuracy : 86%
# # - - - - Model Analysis : Increasing the image size decreases the accuracy. Also, this model seems to be overfitting.
# # 
# # - Model 5 : No of Epochs = 25 , Batch_size = 50 , shape = (70,70) , no of frames = 18
# # 
# # - - - - Training Accuracy : 95.71% , Validation Accuracy : 87%
# # - - - - Model Analysis : This model is clearly an overfit model can see that increasing in number of frames and epochs causing the noise to be learned also from all the frames
# # 
# # - # CNN + RNN : CNN2D LSTM Model - TimeDistributed
# # 
# # - Model 6 : No of Epochs = 25 , Batch_size = 50 , shape = (70,70) , no of frames = 18
# # 
# # - - - - Training Accuracy : 81.79% , Validation Accuracy : 60%
# # - - - - Model Analysis : This model is clearly Overfitting
# # 
# # - Model 7 : No of epochs = 20 , batch_size = 20 , shape  (50,50) , no of frames  = 10
# # 
# # - - - - Training Accuracy : 84.71% , Validation Accuracy : 67%
# # - - - - Model Analysis : This model is clearly overfitting
# # 
# # - # CONV2D + GRU
# # 
# # - Model 8 : No of epochs = 20 , batch_size = 20 , shape  (50,50) , no of frames  = 18
# # 
# # - - - - Training Accuracy : 94.26%, Validation Accuracy : 72%
# # - - - - Model Analysis : This model is overfitting
# # 
# # - # Transfer Learning Using MobileNet
# # 
# # -  Model 9 : No of epochs = 15 , batch_size = 5 , shape  (120,120) , no of frames  = 18
# # 
# # - - - - Training Accuracy : 99.55% , Validation Accuracy : 95%
# # - - - - Model Analysis : This is so far the best model that we got with better accuracy

# # In[ ]:




